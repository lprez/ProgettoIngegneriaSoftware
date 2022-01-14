package com.company.busbooking.dominio;

import java.math.BigDecimal;

public class DescrizioneBigliettoTempo extends DescrizioneBiglietto {
    private final int minuti;

    public DescrizioneBigliettoTempo(long id, BigDecimal prezzo, int minuti) {
        super(id, prezzo);
        this.minuti = minuti;
    }

    @Override
    public String toString() {
        return "Biglietto a tempo. Prezzo: " + prezzo.toString() + ", Durata: " + minuti;
    }

    @Override
    public ValidatoreBiglietto creaValidatore() {
        return new ValidatoreBigliettoTempo(minuti);
    }
}
