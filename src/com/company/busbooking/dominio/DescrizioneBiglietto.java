package com.company.busbooking.dominio;

import java.math.BigDecimal;

public abstract class DescrizioneBiglietto {
    protected final long id;
    protected final BigDecimal prezzo;

    protected DescrizioneBiglietto(long id, BigDecimal prezzo) {
        this.id = id;
        this.prezzo = prezzo;
    }

    public long ottieniId() {
        return id;
    }

    public BigDecimal ottieniPrezzo() {
        return prezzo;
    }

    public abstract ValidatoreBiglietto creaValidatore();
}
