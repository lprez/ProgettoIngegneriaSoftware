package com.company.busbooking.appcliente;

import com.company.busbooking.dto.*;
import com.company.busbooking.interfacce.ServizioCliente;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class ServizioClienteProxyClient implements ServizioCliente {
    private final String indirizzo;

    public ServizioClienteProxyClient(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    @Override
    public Collection<String> richiediListaCitta() {
        try {
            return Unirest.get(indirizzo + "/cliente/listacitta").asJson().getBody().getArray().toList();
        } catch (UnirestException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<DescrizioneBigliettoDTO> richiediCatalogoBiglietti(String citta) {
        try {
            return Unirest.get(indirizzo + "/cliente/catalogobiglietti/{citta}")
                    .routeParam("citta", citta)
                    .asObject(new GenericType<Collection<DescrizioneBigliettoDTO>>() {
                    })
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public int acquistaBiglietto(String citta, long idDescrizione) {
        try {
            return Unirest.post(indirizzo + "/cliente/acquisto")
                    .field("citta", citta)
                    .field("idDescrizione", Long.toString(idDescrizione))
                    .asObject(Integer.class)
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean effettuaPagamento(long idCliente, int idAcquisto, long idCartaCredito, int codiceSicurezza) {
        try {
            return Unirest.post(indirizzo + "/cliente/pagamento")
                    .field("idCliente", Long.toString(idCliente))
                    .field("idAcquisto", Integer.toString(idAcquisto))
                    .field("idCarta", Long.toString(idCartaCredito))
                    .field("codiceSicurezza", Integer.toString(codiceSicurezza))
                    .asObject(Boolean.class)
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void annullaAcquisto(int idAcquisto) {
        try {
            Unirest.delete(indirizzo + "/cliente/acquisto/{id}").routeParam("id", Integer.toString(idAcquisto));
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<BigliettoDTO> richiediListaBiglietti(long idCliente) {
        return richiediListaGenerica("biglietti", new GenericType<Collection<BigliettoDTO>>() {}, idCliente);
    }

    @Override
    public byte[] richiediCodice(long idCliente, int indiceBiglietto) {
        try {
            String codice = Unirest.get(indirizzo + "/cliente/{idCliente}/biglietti/{indiceBiglietto}")
                .routeParam("idCliente", Long.toString(idCliente))
                .routeParam("indiceBiglietto", Integer.toString(indiceBiglietto))
                .asString()
                .getBody();
            return new ObjectMapper().readValue(codice, byte[].class);
        } catch (IOException | UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean creaPreferito(long idCliente, String citta, int idAcquisto, long idCarta) {
        try {
            return Unirest.post(indirizzo + "/cliente/preferito")
                    .field("idCliente", Long.toString(idCliente))
                    .field("citta",  citta)
                    .field("idAcquisto", Long.toString(idAcquisto))
                    .field("idCarta", Long.toString(idCarta))
                    .asObject(Boolean.class)
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminaPreferito(long idCliente, int indicePreferito) {
        try {
            return Unirest.delete(indirizzo + "/cliente/{idCliente}/preferito/{idPreferito}")
                    .routeParam("idCliente", Long.toString(idCliente))
                    .routeParam("idPreferito", Integer.toString(indicePreferito))
                    .asObject(Boolean.class)
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Collection<PreferitoDTO> richiediListaPreferiti(long idCliente) {
        return richiediListaGenerica("preferiti", new GenericType<Collection<PreferitoDTO>>() {}, idCliente);
    }

    @Override
    public Collection<CartaDiCreditoDTO> richiediListaCarte(long idCliente) {
        return richiediListaGenerica("carte", new GenericType<Collection<CartaDiCreditoDTO>>() {}, idCliente);
    }

    @Override
    public boolean creaCarta(long idCliente, String codice, String intestatario, String scadenza) {
        try {
            return Unirest.post(indirizzo + "/cliente/carta")
                    .field("idCliente", Long.toString(idCliente))
                    .field("codice", codice)
                    .field("intestatario", intestatario)
                    .field("scadenza", scadenza)
                    .asObject(Boolean.class)
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modificaCarta(long idCliente, long idCarta, String codice, String intestatario, String scadenza) {
        try {
            return Unirest.put(indirizzo + "/cliente/carta")
                    .field("idCliente", Long.toString(idCliente))
                    .field("idCarta", Long.toString(idCarta))
                    .field("codice", codice)
                    .field("intestatario", intestatario)
                    .field("scadenza", scadenza)
                    .asObject(Boolean.class)
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return false;
        }    }

    @Override
    public boolean eliminaCarta(long idCliente, long idCarta) {
        try {
            return Unirest.delete(indirizzo + "/cliente/{idCliente}/carta/{idCarta}")
                    .routeParam("idCliente", Long.toString(idCliente))
                    .routeParam("idCarta", Long.toString(idCarta))
                    .asObject(Boolean.class)
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Collection<AcquistoDTO> richiediElencoMovimenti(long idCliente) {
        return richiediListaGenerica("movimenti", new GenericType<Collection<AcquistoDTO>>() {}, idCliente);
    }

    public <T> Collection<T> richiediListaGenerica(String nome, GenericType<Collection<T>> tipo, long idCliente) {
        try {
            return Unirest.get(indirizzo + "/cliente/{idCliente}/" + nome)
                    .routeParam("idCliente", Long.toString(idCliente))
                    .asObject(tipo)
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
