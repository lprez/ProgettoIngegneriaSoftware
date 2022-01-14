package com.company.busbooking.appaddetto;

import com.company.busbooking.appaddetto.Comando;
import com.company.busbooking.interfacce.ServizioAddetto;

public class ComandoEsci extends Comando {
    @Override
    public boolean esegui(long idAddetto, ServizioAddetto servizioAddetto) {
        return false;
    }

    @Override
    public String descrizione() {
        return "Esci";
    }
}
