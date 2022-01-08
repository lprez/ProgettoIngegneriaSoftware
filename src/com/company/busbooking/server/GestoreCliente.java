package com.company.busbooking.server;

import com.company.busbooking.dominio.*;
import com.company.busbooking.dto.DescrizioneBigliettoDTO;
import com.company.busbooking.interfacce.ServizioCliente;

import java.util.*;

public class GestoreCliente implements ServizioCliente {
    private final BusBooking busBooking;
    private final List<Acquisto> acquistiInCorso = new ArrayList<Acquisto>();

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
                        descrizioneBiglietto.ottieniTipoBiglietto().toString(),
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
}
