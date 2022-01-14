package com.company.busbooking.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

public class BigliettoDTO {
    private final int indice;
    private final String descrizione;

    public BigliettoDTO(int indice, String descrizione) {
        this.indice = indice;
        this.descrizione = descrizione;
    }

    @JsonGetter("indice")
    public int ottieniIndice() {
        return indice;
    }

    @JsonGetter("descrizione")
    public String ottieniDescrizione() {
        return descrizione;
    }
}
