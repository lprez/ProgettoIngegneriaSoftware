package com.company.busbooking.server;

import com.company.busbooking.dominio.*;
import com.company.busbooking.dto.*;
import com.company.busbooking.interfacce.ServizioCliente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GestoreCliente implements ServizioCliente {
    private final BusBooking busBooking;
    private final List<Acquisto> acquistiInCorso = new ArrayList<Acquisto>();
    private final Map<Long, List<Biglietto>> cacheBiglietti = new HashMap<>();
    private static final SimpleDateFormat formatoScadenza = new SimpleDateFormat("MM/yy");

    public GestoreCliente(BusBooking busBooking) {
        this.busBooking = busBooking;
    }

    @Override
    public Collection<String> richiediListaCitta() {
        return busBooking.ottieniListaCitta();
    }

    @Override
    public Collection<DescrizioneBigliettoDTO> richiediCatalogoBiglietti(String citta) {
        try {
            LinkedList<DescrizioneBigliettoDTO> descrizioni = new LinkedList<>();
            busBooking.ottieniCatalogo(citta).forEach(descrizioneBiglietto -> {
                descrizioni.add(new DescrizioneBigliettoDTO(
                        descrizioneBiglietto.ottieniId(),
                        descrizioneBiglietto.toString(),
                        descrizioneBiglietto.ottieniPrezzo()
                ));
            });
            return descrizioni;
        } catch (BusBooking.CatalogoInesistenteException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public int acquistaBiglietto(String citta, long idDescrizione) {
        try {
            CatalogoBiglietti catalogo = busBooking.ottieniCatalogo(citta);
            Acquisto acquisto = busBooking.acquistaBiglietto(catalogo.ottieniBiglietto(idDescrizione));
            acquistiInCorso.add(acquisto);
            return acquistiInCorso.size() - 1;
        } catch (BusBooking.ErroreCrittografiaException e) {
            return -1;
        } catch (BusBooking.CatalogoInesistenteException e) {
            return -1;
        }
    }

    @Override
    public boolean effettuaPagamento(long idCliente, int idAcquisto, long idCartaCredito, int codiceSicurezza) {
        Acquisto acquisto;
        if (idAcquisto < acquistiInCorso.size() && (acquisto = acquistiInCorso.get(idAcquisto)) != null) {
            Cliente cliente = null;
            try {
                cliente = busBooking.ottieniCliente(idCliente);
            } catch (BusBooking.ClienteInesistenteException e) {
                return false;
            }
            CartaDiCredito carta = cliente.ottieniCarta(idCartaCredito);

            boolean esito = busBooking.effettuaPagamento(
                    cliente,
                    acquisto,
                    carta,
                    codiceSicurezza
            );

            acquistiInCorso.set(idAcquisto, null);
            return esito;
        } else {
            return false;
        }
    }

    @Override
    public void annullaAcquisto(int idAcquisto) {
        if (idAcquisto < acquistiInCorso.size()) acquistiInCorso.set(idAcquisto, null);
    }

    @Override
    public Collection<BigliettoDTO> richiediListaBiglietti(long idCliente) {
        try {
            List<Biglietto> lista = busBooking.ottieniListaBiglietti(busBooking.ottieniCliente(idCliente));
            cacheBiglietti.put(idCliente, lista);
            return IntStream.range(0, lista.size()).mapToObj(
                    indice -> new BigliettoDTO(indice, lista.get(indice).ottieniDescrizione().toString())
            ).collect(Collectors.toList());
        } catch (BusBooking.ClienteInesistenteException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public byte[] richiediCodice(long idCliente, int indiceBiglietto) {
        List<Biglietto> lista;
        try {
            if (cacheBiglietti.containsKey(idCliente) && indiceBiglietto < (lista = cacheBiglietti.get(idCliente)).size()) {
                return lista.get(indiceBiglietto).ottieniCodice();
            }
        } catch (Biglietto.BigliettoSenzaFirmaException e) {
                e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public boolean creaPreferito(long idCliente, String citta, int idAcquisto, long idCarta) {
        Acquisto acquisto;
        if (idAcquisto < acquistiInCorso.size() && (acquisto = acquistiInCorso.get(idAcquisto)) != null) {
            Cliente cliente = null;
            try {
                cliente = busBooking.ottieniCliente(idCliente);
            } catch (BusBooking.ClienteInesistenteException e) {
                return false;
            }
            CartaDiCredito carta = cliente.ottieniCarta(idCarta);
            cliente.creaPreferito(citta, acquisto, carta);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean eliminaPreferito(long idCliente, int indicePreferito) {
        try {
            Cliente cliente = busBooking.ottieniCliente(idCliente);
            busBooking.eliminaPreferito(cliente, indicePreferito);
            return true;
        } catch (BusBooking.ClienteInesistenteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Collection<PreferitoDTO> richiediListaPreferiti(long idCliente) {
        try {
            Cliente cliente = busBooking.ottieniCliente(idCliente);
            return busBooking.ottieniListaPreferiti(cliente).stream().map(
                    preferito -> new PreferitoDTO(
                            preferito.ottieniCitta(),
                            preferito.ottieniDescrizioneBiglietto().ottieniId(),
                            preferito.ottieniDescrizioneBiglietto().toString(),
                            preferito.ottieniCartaDiCredito().ottieniId(),
                            preferito.ottieniCartaDiCredito().ottieniCodiceOscurato()
                    )
            ).collect(Collectors.toList());
        } catch (BusBooking.ClienteInesistenteException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<CartaDiCreditoDTO> richiediListaCarte(long idCliente) {
        try {
            Cliente cliente = busBooking.ottieniCliente(idCliente);
            return busBooking.ottieniListaCarte(cliente).stream().map(
                    carta -> new CartaDiCreditoDTO(
                            carta.ottieniId(),
                            carta.ottieniCodiceOscurato(),
                            carta.ottieniIntestatario(),
                            formatoScadenza.format(carta.ottieniScadenza())
                    )
            ).collect(Collectors.toList());
        } catch (BusBooking.ClienteInesistenteException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }    }

    @Override
    public boolean creaCarta(long idCliente, String codice, String intestatario, String scadenza) {
        try {
            Date dataScadenza = formatoScadenza.parse(scadenza);
            Cliente cliente = busBooking.ottieniCliente(idCliente);
            busBooking.aggiungiCarta(
                    cliente,
                    busBooking.creaCarta(codice, intestatario, dataScadenza)
            );
            return true;
        } catch (ParseException | BusBooking.ClienteInesistenteException | BusBooking.CartaNonValidaException e) {
            return false;
        }
    }

    @Override
    public boolean modificaCarta(long idCliente, long idCarta, String codice, String intestatario, String scadenza) {
        try {
            Cliente cliente = busBooking.ottieniCliente(idCliente);
            return busBooking.modificaCarta(cliente, idCarta, codice, intestatario, formatoScadenza.parse(scadenza));
        } catch (BusBooking.ClienteInesistenteException | ParseException | BusBooking.CartaNonValidaException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminaCarta(long idCliente, long idCarta) {
        Cliente cliente = null;
        try {
            cliente = busBooking.ottieniCliente(idCliente);
            return busBooking.eliminaCarta(cliente, idCarta);
        } catch (BusBooking.ClienteInesistenteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Collection<AcquistoDTO> richiediElencoMovimenti(long idCliente) {
        try {
            Cliente cliente = busBooking.ottieniCliente(idCliente);
            return cliente.ottieniAcquisti().stream().map(
                    acquisto -> {
                        CartaDiCredito carta = acquisto.ottieniPagamento().ottieniCartaDiCredito();
                        return new AcquistoDTO(
                            acquisto.ottieniBiglietto().ottieniDescrizione().ottieniId(),
                            acquisto.ottieniBiglietto().ottieniDescrizione().toString(),
                            carta != null ? carta.ottieniId() : -1,
                            carta != null ? carta.ottieniCodiceOscurato() : "(carta modificata/eliminata)"
                        );
                    }
            ).collect(Collectors.toList());
        } catch (BusBooking.ClienteInesistenteException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
