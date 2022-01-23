package com.company.busbooking.appcliente;

import com.company.busbooking.interfacce.ServizioCliente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static final String indirizzo = "http://localhost:8080";
    private static final long idCliente = 0;

    private static final List<Comando> comandi = new ArrayList<Comando>(
            List.of(new ComandoAcquistaBiglietto(),
                    new ComandoMostraBiglietto(),
                    new ComandoGestisciCarte(),
                    new ComandoGestisciPreferiti(),
                    new ComandoMostraMovimenti(),
                    new ComandoEsci("Esci"))
    );

    public static void main(String[] args) throws IOException {
        ServizioCliente servizio = new ServizioClienteProxyClient(indirizzo);

        new MenuComandi(comandi).esegui(idCliente, servizio);
    }
}