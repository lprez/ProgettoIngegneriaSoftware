package com.company.busbooking.appcliente;

import com.company.busbooking.dto.PreferitoDTO;
import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Input;

import java.io.IOException;

public class ComandoEliminaPreferito extends Comando {
    @Override
    public boolean esegui(long idCliente, ServizioCliente servizio) {
        try {
            Integer indice = ComandoGestisciPreferiti.creaMenuPreferiti(idCliente, servizio).mostraSelezionaIndice();
            if (indice == null) return true;

            System.out.print("Conferma? [s/N] ");
            if (!Input.leggisN()) return true;

            if (servizio.eliminaPreferito(idCliente, indice)) {
                System.out.println("Preferito elminato.");
            } else {
                System.out.println("Non è stato possibile eliminare il preferito.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String descrizione() {
        return "Elimina preferito";
    }
}
