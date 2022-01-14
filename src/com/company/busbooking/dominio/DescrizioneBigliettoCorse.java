package com.company.busbooking.dominio;

import java.math.BigDecimal;

public class DescrizioneBigliettoCorse extends DescrizioneBiglietto {
    private final int nCorse;

    public DescrizioneBigliettoCorse(long id, BigDecimal prezzo, int nCorse) {
        super(id, prezzo);
        this.nCorse = nCorse;
    }

    @Override
    public String toString() {
        if (nCorse == 2) {
            return "Biglietto andata/ritorno. Prezzo: " + prezzo.toString();
        } else {
            return "Biglietto di sola andata. Prezzo: " + prezzo.toString();
        }
    }

    @Override
    public ValidatoreBiglietto creaValidatore() {
        return new ValidatoreBigliettoCorse(nCorse);
    }
}
