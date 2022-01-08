package com.company.busbooking.appcliente;

import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static final String indirizzo = "http://localhost:8080";
    private static final long idCliente = 0;

    private static final List<Comando> comandi = new ArrayList<Comando>(
            List.of(new ComandoAcquistaBiglietto(), new ComandoEsci())
    );

    public static void main(String[] args) throws IOException {
        ServizioCliente servizio = new ServizioClienteProxyClient(indirizzo);

        Menu<Comando> menuComandi = new Menu<Comando>(
                comandi.stream().map(comando ->
                        new Menu.ElementoMenu<Comando>(comando.descrizione(), comando)
                ).toList()
        );

        Comando comando;
        while ((comando = menuComandi.mostra()) != null) {
            if (!comando.esegui(idCliente, servizio)) {
                break;
            }
        }
    }
}