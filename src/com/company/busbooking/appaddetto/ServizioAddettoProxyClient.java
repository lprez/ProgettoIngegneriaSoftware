package com.company.busbooking.appaddetto;

import com.company.busbooking.dto.ConvalidaBigliettoDTO;
import com.company.busbooking.interfacce.ServizioAddetto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

public class ServizioAddettoProxyClient implements ServizioAddetto {
    private final String indirizzo;
    private final ServizioAddetto fallback;

    public ServizioAddettoProxyClient(String indirizzo, ServizioAddetto fallback) {
        this.indirizzo = indirizzo;
        this.fallback = fallback;
    }

    @Override
    public ConvalidaBigliettoDTO convalidaBiglietto(long idAddetto, byte[] codice) {
        try {
            return Unirest.post(indirizzo + "/addetto/{idAddetto}/convalida")
                    .routeParam("idAddetto", Long.toString(idAddetto))
                    .field("codice", new ObjectMapper().writeValueAsString(codice))
                    .asObject(ConvalidaBigliettoDTO.class)
                    .getBody();
        } catch (UnirestException e) {
            // e.printStackTrace();
            if (fallback != null) {
                return fallback.convalidaBiglietto(idAddetto, codice);
            } else {
                return new ConvalidaBigliettoDTO(e.getMessage());
            }
        } catch (JsonProcessingException e) {
            return new ConvalidaBigliettoDTO(e.getMessage());
        }
    }

    @Override
    public boolean aggiornaCorsa(long idAddetto, long idCorsa) {
        try {
            return Unirest.put(indirizzo + "/addetto/{idAddetto}/corsa/{idCorsa}")
                    .routeParam("idAddetto", Long.toString(idAddetto))
                    .routeParam("idCorsa", Long.toString(idCorsa))
                    .asObject(Boolean.class)
                    .getBody();
        } catch (UnirestException e) {
            return false;
        }
    }
}
