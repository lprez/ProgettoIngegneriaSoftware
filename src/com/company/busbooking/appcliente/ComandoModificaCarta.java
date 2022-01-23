package com.company.busbooking.appcliente;

import com.company.busbooking.dto.CartaDiCreditoDTO;
import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Input;

import java.io.IOException;

public class ComandoModificaCarta extends Comando {

    @Override
    public boolean esegui(long idCliente, ServizioCliente servizioCliente) {
        CartaDiCreditoDTO carta = null;
        try {
            carta = ComandoGestisciCarte.creaMenuCarte(idCliente, servizioCliente).mostraSeleziona();
            if (carta != null) {
                System.out.print("Inserisci il nuovo codice della carta nel formato XXXX-XXXX-XXXX-XXXX (lascia vuoto per non cambiare): ");
                String codice = Input.leggiStringa();
                if (codice.isBlank()) {
                    System.out.println(carta.ottieniCodice());
                }
                System.out.print("Inserisci la data di scadenza nel formato MM/AA (o lascia vuoto): ");
                String scadenza = Input.leggiStringa();
                if (scadenza.isBlank()) {
                    scadenza = carta.ottieniScadenza();
                    System.out.println(scadenza);
                }
                System.out.print("Inserisci il nome dell'intestatario (o lascia vuoto): ");
                String intestatario = Input.leggiStringa();
                if (intestatario.isBlank()) {
                    intestatario = carta.ottieniIntestatario();
                    System.out.println(intestatario);
                }

                System.out.print("Conferma? [s/N] ");
                if (!Input.leggisN()) return true;

                if (servizioCliente.modificaCarta(idCliente, carta.ottieniId(), codice, intestatario, scadenza)) {
                    System.out.println("La carta è stata modificata.");
                } else {
                    System.out.println("Non è stato possibile modificare la carta.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String descrizione() {
        return "Modifica carta";
    }
}
