package com.company.busbooking.dominio;

import java.math.BigDecimal;

public class DescrizioneBiglietto {
    private final long id;
    private final BigDecimal prezzo;
    private final TipoBiglietto tipoBiglietto;

    public DescrizioneBiglietto(long id, BigDecimal prezzo, TipoBiglietto tipoBiglietto) {
        this.id = id;
        this.prezzo = prezzo;
        this.tipoBiglietto = tipoBiglietto;
    }

    public long ottieniId() {
        return id;
    }

    public BigDecimal ottieniPrezzo() {
        return prezzo;
    }

    public TipoBiglietto ottieniTipoBiglietto() {
        return tipoBiglietto;
    }

    public enum TipoBiglietto {
        TEMPO,
        CORSA_SINGOLA,
        ANDATA_RITORNO
    }
}
