package com.company.busbooking.server;

import com.company.busbooking.dominio.*;
import com.company.busbooking.dto.BigliettoDTO;
import com.company.busbooking.dto.DescrizioneBigliettoDTO;
import com.company.busbooking.interfacce.ServizioCliente;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GestoreCliente implements ServizioCliente {
    private final BusBooking busBooking;
    private final List<Acquisto> acquistiInCorso = new ArrayList<Acquisto>();
    private final Map<Long, List<Biglietto>> biglietti = new HashMap<>();

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
            biglietti.put(idCliente, lista);
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
            if (biglietti.containsKey(idCliente) && indiceBiglietto < (lista = biglietti.get(idCliente)).size()) {
                return lista.get(indiceBiglietto).ottieniCodice();
            }
        } catch (Biglietto.BigliettoSenzaFirmaException e) {
                e.printStackTrace();
        }
        return new byte[0];
    }
}
