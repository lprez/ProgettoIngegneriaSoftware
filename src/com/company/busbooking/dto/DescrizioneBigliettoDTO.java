package com.company.busbooking.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.*;

public class DescrizioneBigliettoDTO {
    private final long id;
    private final String descrizione;
    private final BigDecimal prezzo;

    public DescrizioneBigliettoDTO(long id, String descrizione, BigDecimal prezzo) {
        this.id = id;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
    }

    @JsonGetter("id")
    public long ottieniId() {
        return id;
    }

    @JsonGetter("descrizione")
    public String ottieniDescrizione() {
        return descrizione;
    }

    @JsonGetter("prezzo")
    public BigDecimal ottieniPrezzo() {
        return prezzo;
    }
}
