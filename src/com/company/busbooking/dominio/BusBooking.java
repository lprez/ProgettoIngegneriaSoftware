package com.company.busbooking.dominio;

import java.security.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusBooking {
    private final KeyPair chiave;
    private final ServizioPagamento servizioPagamento;

    private final Map<String, CatalogoBiglietti> cataloghi = new HashMap<>();
    private final Map<Long, Cliente> clienti = new HashMap<>();
    private final Map<Long, Addetto> addetti = new HashMap<>();
    private final Map<Long, Biglietto> biglietti = new HashMap<>();
    private final Map<Long, Corsa> corse = new HashMap<>();

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
            this.biglietti.put(acquisto.ottieniBiglietto().ottieniId(), acquisto.ottieniBiglietto());
        }

        return esito;
    }

    public void aggiornaCorsa(Addetto addetto, Corsa corsa) {
        addetto.aggiornaCorsa(corsa);
    }

    public boolean convalidaBiglietto(Addetto addetto, byte[] codice) throws BigliettoInesistenteException {
        Corsa corsa = addetto.ottieniCorsa();
        Biglietto biglietto = this.ottieniBiglietto(codice);

        return biglietto.convalida(corsa);
    }

    public List<Biglietto> ottieniListaBiglietti(Cliente cliente) {
        return cliente.ottieniListaBiglietti();
    }

    public Biglietto ottieniBiglietto(long id) throws BigliettoInesistenteException {
        if (!biglietti.containsKey(id)) {
            throw new BigliettoInesistenteException("Biglietto " + id + " non trovato.");
        } else {
            return biglietti.get(id);
        }
    }

    public Biglietto ottieniBiglietto(byte[] codice) throws BigliettoInesistenteException {
        return ottieniBiglietto(Biglietto.estraiId(codice));
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

    public void aggiungiAddetto(Addetto addetto) {
        addetti.put(addetto.ottieniId(), addetto);
    }

    public Addetto ottieniAddetto(long idAddetto) throws AddettoInesistenteException {
        if (!addetti.containsKey(idAddetto)) {
            throw new AddettoInesistenteException("Addetto " + idAddetto + " non trovato.");
        } else {
            return addetti.get(idAddetto);
        }
    }

    public void aggiungiCatalogo(CatalogoBiglietti catalogo) {
        cataloghi.put(catalogo.ottieniCitta(), catalogo);
    }

    public KeyPair ottieniChiavi() {
        return this.chiave;
    }

    public void aggiungiCorsa(Corsa corsa) {
        corse.put(corsa.ottieniId(), corsa);
    }

    public Corsa ottieniCorsa(long idCorsa) throws CorsaInesistenteException {
        if (!corse.containsKey(idCorsa)) {
            throw new CorsaInesistenteException("Non c'è nessuna corsa con id " + idCorsa);
        } else {
            return corse.get(idCorsa);
        }
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

    public class BigliettoInesistenteException extends Exception {
        public BigliettoInesistenteException(String msg) {
            super(msg);
        }
    }

    public class AddettoInesistenteException extends Exception {
        public AddettoInesistenteException(String msg) {
            super(msg);
        }
    }

    public class CorsaInesistenteException extends Exception {
        public CorsaInesistenteException(String msg) {
            super(msg);
        }
    }

    public class ErroreCrittografiaException extends Exception {
        public ErroreCrittografiaException(String msg, Throwable err) {
            super(msg, err);
        }
    }
}
