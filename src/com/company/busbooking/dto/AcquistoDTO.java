package com.company.busbooking.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

public class AcquistoDTO {
    private final long idDescrizioneBiglietto;
    private final String descrizioneBiglietto;
    private final long idCarta;
    private final String carta;

    public AcquistoDTO(long idDescrizione, String descrizioneBiglietto, long idCarta, String carta) {
        this.descrizioneBiglietto = descrizioneBiglietto;
        this.idDescrizioneBiglietto = idDescrizione;
        this.idCarta = idCarta;
        this.carta = carta;
    }

    @JsonGetter("descrizioneBiglietto")
    public String ottieniDescrizione() {
        return descrizioneBiglietto;
    }

    @JsonGetter("idDescrizione")
    public long ottieniIdDescrizione() {
        return idDescrizioneBiglietto;
    }

    @JsonGetter("idCarta")
    public long ottieniIdCarta() {
        return idCarta;
    }

    @JsonGetter("carta")
    public String ottieniCarta() {
        return carta;
    }
}
