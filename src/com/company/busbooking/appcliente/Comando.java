package com.company.busbooking.appcliente;

import com.company.busbooking.interfacce.ServizioCliente;

public abstract class Comando {
    public abstract boolean esegui(long idCliente, ServizioCliente servizioCliente);
    public abstract String descrizione();
}
