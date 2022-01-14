package com.company.busbooking.appcliente;

import com.company.busbooking.dto.BigliettoDTO;
import com.company.busbooking.dto.DescrizioneBigliettoDTO;
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
        try {
            return Unirest.get(indirizzo + "/cliente/biglietti/{idCliente}")
                    .routeParam("idCliente", Long.toString(idCliente))
                    .asObject(new GenericType<Collection<BigliettoDTO>>() {
                    })
                    .getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public byte[] richiediCodice(long idCliente, int indiceBiglietto) {
        try {
            String codice = Unirest.get(indirizzo + "/cliente/biglietti/{idCliente}/{indiceBiglietto}")
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
}
