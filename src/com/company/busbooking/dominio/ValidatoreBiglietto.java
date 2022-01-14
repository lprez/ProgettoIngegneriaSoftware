package com.company.busbooking.dominio;

import java.util.Date;
import java.util.List;

public abstract class ValidatoreBiglietto {
    public abstract boolean valido(List<Convalida> convalide, Corsa corsa, Date adesso);

    public boolean valido(List<Convalida> convalide, Corsa corsa) {
        return valido(convalide, corsa, new Date());
    }
}
