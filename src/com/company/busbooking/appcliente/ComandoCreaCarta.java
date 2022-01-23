package com.company.busbooking.appcliente;

import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Input;

import java.io.IOException;

public class ComandoCreaCarta extends Comando {
    @Override
    public boolean esegui(long idCliente, ServizioCliente servizioCliente) {
        try {
            System.out.print("Inserisci il codice della carta nel formato XXXX-XXXX-XXXX-XXXX: ");
            String codice = Input.leggiStringa();
            System.out.print("Inserisci la data di scadenza nel formato MM/AA: ");
            String scadenza = Input.leggiStringa();
            System.out.print("Inserisci il nome dell'intestatario: ");
            String intestatario = Input.leggiStringa();

            System.out.print("Conferma? [s/N] ");
            if (!Input.leggisN()) return true;

            if (servizioCliente.creaCarta(idCliente, codice, intestatario, scadenza)) {
                System.out.println("Carta aggiunta con successo.");
            } else {
                System.out.println("Non è stato possibile aggiungere la carta.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public String descrizione() {
        return "Crea una nuova carta";
    }
}
