package com.company.busbooking.appcliente;

import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Menu;

import java.io.IOException;
import java.util.List;

public class MenuComandi {
    private final Menu<Comando> menuComandi;

    public MenuComandi(List<Comando> comandi) {
        this.menuComandi = new Menu<Comando>(
                comandi.stream().map(comando ->
                        new Menu.ElementoMenu<Comando>(comando.descrizione(), comando)
                ).toList()
        );
    }

    public void esegui(long idCliente, ServizioCliente servizioCliente) {
        Comando comando;

        while (true) {
            try {
                if ((comando = menuComandi.mostraSeleziona()) == null || !comando.esegui(idCliente, servizioCliente))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
