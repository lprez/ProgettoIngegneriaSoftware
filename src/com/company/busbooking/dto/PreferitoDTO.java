package com.company.busbooking.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

public class PreferitoDTO {
    private final String citta;
    private final long idDescrizioneBiglietto;
    private final String descrizioneBiglietto;
    private final long idCarta;
    private final String carta;

    public PreferitoDTO(String citta, long idDescrizione, String descrizioneBiglietto, long idCarta, String carta) {
        this.citta = citta;
        this.descrizioneBiglietto = descrizioneBiglietto;
        this.idDescrizioneBiglietto = idDescrizione;
        this.idCarta = idCarta;
        this.carta = carta;
    }

    @JsonGetter("citta")
    public String ottieniCitta() { return citta; }

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
