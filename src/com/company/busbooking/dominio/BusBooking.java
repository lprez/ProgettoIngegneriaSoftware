package com.company.busbooking.dominio;

import java.math.BigDecimal;
import java.security.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BusBooking {
    private final Map<String, CatalogoBiglietti> cataloghi = new HashMap<>();
    private final Map<Long, Cliente> clienti = new HashMap<>();
    private final KeyPair chiave;
    private final ServizioPagamento servizioPagamento;

    public BusBooking(KeyPair chiave, ServizioPagamento servizioPagamento) {
        this.chiave = chiave;
        this.servizioPagamento = servizioPagamento;
    }

    public Collection<String> ottieniListaCitta() {
        return cataloghi.keySet();
    }

    public CatalogoBiglietti ottieniCatalogo(String citta) throws CatalogoInesistenteException {
        if (!cataloghi.containsKey(citta)) {
            throw new CatalogoInesistenteException("Catalogo relativo alla città " + citta + " non trovato.");
        }
        return cataloghi.get(citta);
    }

    public Acquisto acquistaBiglietto(DescrizioneBiglietto descrizioneBiglietto)
        throws ErroreCrittografiaException {
        Biglietto biglietto = new Biglietto(descrizioneBiglietto);

        try {
            biglietto.generaFirma(chiave.getPrivate());
        } catch (NoSuchAlgorithmException e) {
            throw new ErroreCrittografiaException("Algoritmo inesistente", e);
        } catch (SignatureException e) {
            throw new ErroreCrittografiaException("Errore nella generazione della firma", e);
        } catch (InvalidKeyException e) {
            throw new ErroreCrittografiaException("Chiave non valida", e);
        }

        return new Acquisto(biglietto);
    }

    public boolean effettuaPagamento(Cliente cliente, Acquisto acquisto, CartaDiCredito carta, int codiceSicurezza) {
        Pagamento pagamento = acquisto.creaPagamento(carta);

        boolean esito = this.servizioPagamento.paga(pagamento, codiceSicurezza);
        if (esito) {
            cliente.aggiungiAcquisto(acquisto);
        }

        return esito;
    }

    public void aggiungiCliente(Cliente cliente) {
        clienti.put(cliente.ottieniId(), cliente);
    }

    public Cliente ottieniCliente(long idCliente) throws ClienteInesistenteException {
        if (!clienti.containsKey(idCliente)) {
            throw new ClienteInesistenteException("Cliente " + idCliente + " non trovato.");
        } else {
            return clienti.get(idCliente);
        }
    }

    public void aggiungiCatalogo(CatalogoBiglietti catalogo) {
        cataloghi.put(catalogo.ottieniCitta(), catalogo);
    }

    public KeyPair ottieniChiavi() {
        return this.chiave;
    }

    public class CatalogoInesistenteException extends Exception {
        public CatalogoInesistenteException(String msg) {
            super(msg);
        }
    }

    public class ClienteInesistenteException extends Exception {
        public ClienteInesistenteException(String msg) {
            super(msg);
        }
    }

    public class ErroreCrittografiaException extends Exception {
        public ErroreCrittografiaException(String msg, Throwable err) {
            super(msg, err);
        }
    }
}
