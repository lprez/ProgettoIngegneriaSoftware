package com.company.busbooking.dominio;

import java.security.*;
import java.util.*;

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

    public KeyPair ottieniChiavi() {
        return this.chiave;
    }

    // Cataloghi

    public Collection<String> ottieniListaCitta() {
        return cataloghi.keySet();
    }

    public CatalogoBiglietti ottieniCatalogo(String citta) throws CatalogoInesistenteException {
        if (!cataloghi.containsKey(citta)) {
            throw new CatalogoInesistenteException("Catalogo relativo alla città " + citta + " non trovato.");
        }
        return cataloghi.get(citta);
    }

    public void aggiungiCatalogo(CatalogoBiglietti catalogo) {
        cataloghi.put(catalogo.ottieniCitta(), catalogo);
    }

    public class CatalogoInesistenteException extends Exception {
        public CatalogoInesistenteException(String msg) {
            super(msg);
        }
    }

    // Acquisto

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

    public class ErroreCrittografiaException extends Exception {
        public ErroreCrittografiaException(String msg, Throwable err) {
            super(msg, err);
        }
    }

    // Convalida

    public boolean convalidaBiglietto(Addetto addetto, byte[] codice) throws BigliettoInesistenteException {
        Corsa corsa = addetto.ottieniCorsa();
        Biglietto biglietto = this.ottieniBiglietto(codice);

        return biglietto.convalida(corsa);
    }

    // Biglietti

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

    public class BigliettoInesistenteException extends Exception {
        public BigliettoInesistenteException(String msg) {
            super(msg);
        }
    }

    // Preferiti

    public void creaPreferito(Cliente cliente, String citta, Acquisto acquisto, CartaDiCredito carta) {
        cliente.creaPreferito(citta, acquisto, carta);
    }

    public Acquisto acquistaPreferito(Preferito preferito) throws ErroreCrittografiaException {
        return this.acquistaBiglietto(preferito.ottieniDescrizioneBiglietto());
    }

    public List<Preferito> ottieniListaPreferiti(Cliente cliente) {
        return cliente.ottieniPreferiti();
    }

    public void eliminaPreferito(Cliente cliente, int indice) {
        cliente.eliminaPreferito(indice);
    }

    // Carte

    public Collection<CartaDiCredito> ottieniListaCarte(Cliente cliente) {
        return cliente.ottieniListaCarte();
    }

    public CartaDiCredito creaCarta(String codice, String intestatario, Date scadenza) throws CartaNonValidaException {
        CartaDiCredito carta = new CartaDiCredito(codice, intestatario, scadenza);
        if (!carta.codiceValido()) throw new CartaNonValidaException("Il codice della carta non è valido.");
        return carta;
    }

    public void aggiungiCarta(Cliente cliente, CartaDiCredito carta) {
        cliente.aggiungiCarta(carta);
    }

    public boolean modificaCarta(Cliente cliente, long idCarta, String codice, String intestatario, Date scadenza) throws CartaNonValidaException {
        CartaDiCredito carta;
        if ((carta = cliente.ottieniCarta(idCarta)) != null) {
            if (codice.isBlank()) codice = carta.ottieniCodice();
            cliente.eliminaCarta(idCarta);
            cliente.aggiungiCarta(this.creaCarta(codice, intestatario, scadenza));
            return true;
        } else {
            return false;
        }
    }

    public boolean eliminaCarta(Cliente cliente, long idCarta) {
        return cliente.eliminaCarta(idCarta);
    }

    public class CartaNonValidaException extends Exception {
        public CartaNonValidaException(String msg) {
            super(msg);
        }
    }

    // Clienti

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

    // tutti i biglietti acquistati
    public List<Acquisto> ottieniElencoMovimenti(Cliente cliente) {
        return cliente.ottieniAcquisti();
    }

    // solo i biglietti che potrebbero essere ancora validi
    public List<Biglietto> ottieniListaBiglietti(Cliente cliente) {
        return cliente.ottieniListaBiglietti();
    }

    public class ClienteInesistenteException extends Exception {
        public ClienteInesistenteException(String msg) {
            super(msg);
        }
    }

    // Addetti

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

    public class AddettoInesistenteException extends Exception {
        public AddettoInesistenteException(String msg) {
            super(msg);
        }
    }

    // Corse

    public void aggiungiCorsa(Corsa corsa) {
        corse.put(corsa.ottieniId(), corsa);
    }

    public void aggiornaCorsa(Addetto addetto, Corsa corsa) {
        addetto.aggiornaCorsa(corsa);
    }

    public Corsa ottieniCorsa(long idCorsa) throws CorsaInesistenteException {
        if (!corse.containsKey(idCorsa)) {
            throw new CorsaInesistenteException("Non c'è nessuna corsa con id " + idCorsa);
        } else {
            return corse.get(idCorsa);
        }
    }

    public class CorsaInesistenteException extends Exception {
        public CorsaInesistenteException(String msg) {
            super(msg);
        }
    }
}
