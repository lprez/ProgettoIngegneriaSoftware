package com.company.busbooking.appaddetto;

import com.company.busbooking.dto.ConvalidaBigliettoDTO;
import com.company.busbooking.interfacce.ServizioAddetto;
import com.company.busbooking.util.Input;

import java.io.IOException;

public class ComandoConvalidaBiglietto extends Comando {
    @Override
    public boolean esegui(long idAddetto, ServizioAddetto servizioAddetto) {
        System.out.print("Inserisci il codice del biglietto: ");
        try {
            byte[] codice = Input.leggiArrayByte();

            if (codice == null) {
                System.out.println("Codice malformato");
            }

            ConvalidaBigliettoDTO valido = servizioAddetto.convalidaBiglietto(idAddetto, codice);

            if (valido.ottieniErrore()) {
                System.out.println(valido.ottieniMessaggio());
            } else {
                if (valido.ottieniValido()) {
                    System.out.println("Il biglietto è valido.");
                } else {
                    System.out.println("Il biglietto non è valido.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String descrizione() {
        return "Convalida biglietto";
    }
}
