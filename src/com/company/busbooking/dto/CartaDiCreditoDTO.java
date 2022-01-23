package com.company.busbooking.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

public class CartaDiCreditoDTO {
    private final String codice, intestatario, scadenza;
    private final long id;

    public CartaDiCreditoDTO(long id, String codice, String intestatario, String scadenza) {
        this.id = id;
        this.codice = codice;
        this.intestatario = intestatario;
        this.scadenza = scadenza;
    }

    @JsonGetter("codice")
    public String ottieniCodice() {
        return codice;
    }

    @JsonGetter("intestatario")
    public String ottieniIntestatario() {
        return intestatario;
    }

    @JsonGetter("scadenza")
    public String ottieniScadenza() {
        return scadenza;
    }

    @JsonGetter("id")
    public long ottieniId() { return id; }
}
