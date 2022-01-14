package com.company.busbooking.dominio;

import java.util.Date;
import java.util.List;

public class ValidatoreBigliettoCorse extends ValidatoreBiglietto {
    private final int nCorse;

    public ValidatoreBigliettoCorse(int nCorse) {
        super();
        this.nCorse = nCorse;
    }

    @Override
    public boolean valido(List<Convalida> convalide, Corsa corsa, Date adesso) {
        // Se il biglietto non è stato ancora usato, allora è valido ovunque
        if (convalide.size() == 0) {
            return true;
        } else {
            Convalida ultimaConvalida = convalide.get(convalide.size() - 1);
            // Se il biglietto è stato usato in un giorno precedente, non è più valido
            if (ultimaConvalida.ottieniData().getDay() != adesso.getDay()) return false;
            // Altrimenti è ancora valido in qualche corsa futura
            if (corsa == null) return true;
            // Se siamo ancora nell'ultima corsa in cui è stato convalidato, allora è ancora valido
            if (ultimaConvalida.ottieniCorsa().equals(corsa)) return true;
            // Se siamo in una corsa diversa ed è stato già esaurito il numero di corse, allora non è valido.
            return (convalide.size() < nCorse);
        }
    }
}
