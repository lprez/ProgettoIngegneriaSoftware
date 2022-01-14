package com.company.busbooking.dominio;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ValidatoreBigliettoTempo extends ValidatoreBiglietto {
    private final int durataMinuti;

    public ValidatoreBigliettoTempo(int durataMinuti) {
        this.durataMinuti = durataMinuti;
    }

    @Override
    public boolean valido(List<Convalida> convalide, Corsa corsa, Date adesso) {
        if (convalide.size() == 0) return true;
        long intervallo = adesso.getTime() - convalide.get(0).ottieniData().getTime();
        return TimeUnit.MILLISECONDS.toMinutes(intervallo) <= durataMinuti;
    }
}
