package com.company.busbooking.appcliente;

import com.company.busbooking.interfacce.ServizioCliente;

public class ComandoEsci extends Comando {
    @Override
    public boolean esegui(long idCliente, ServizioCliente servizioCliente) {
        return false;
    }

    @Override
    public String descrizione() {
        return "Esci";
    }
}
