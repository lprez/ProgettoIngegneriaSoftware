package com.company.busbooking.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.*;

public class DescrizioneBigliettoDTO {
    private final long id;
    private final String tipo;
    private final BigDecimal prezzo;

    public DescrizioneBigliettoDTO(long id, String tipo, BigDecimal prezzo) {
        this.id = id;
        this.tipo = tipo;
        this.prezzo = prezzo;
    }

    @JsonGetter("id")
    public long ottieniId() {
        return id;
    }

    @JsonGetter("tipo")
    public String ottieniTipo() {
        return tipo;
    }

    @JsonGetter("prezzo")
    public BigDecimal ottieniPrezzo() {
        return prezzo;
    }
}
