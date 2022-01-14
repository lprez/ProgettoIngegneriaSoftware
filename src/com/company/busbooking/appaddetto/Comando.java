package com.company.busbooking.appaddetto;

import com.company.busbooking.interfacce.ServizioAddetto;

public abstract class Comando {
    public abstract boolean esegui(long idAddetto, ServizioAddetto servizioAddetto);
    public abstract String descrizione();
}
