package com.company.busbooking.appcliente;

import com.company.busbooking.interfacce.ServizioCliente;

public class ComandoEsci extends Comando {
    private final String descrizione;

    public ComandoEsci(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public boolean esegui(long idCliente, ServizioCliente servizioCliente) {
        return false;
    }

    @Override
    public String descrizione() {
        return descrizione;
    }
}
