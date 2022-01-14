package com.company.busbooking.appaddetto;

import com.company.busbooking.interfacce.ServizioAddetto;
import com.company.busbooking.util.Input;

import java.io.IOException;

public class ComandoAggiornaCorsa extends Comando {
    @Override
    public boolean esegui(long idAddetto, ServizioAddetto servizioAddetto) {
        System.out.print("Inserisci l'ID della corsa (o -1 per nessuna): ");
        try {
            long idCorsa = Input.leggiIntero();
            if (servizioAddetto.aggiornaCorsa(idAddetto, idCorsa)) {
                System.out.println("La corsa è stata aggiornata.");
            } else {
                System.out.println("Non è stato possibile aggiornare la corsa.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String descrizione() {
        return "Aggiorna corsa";
    }
}
