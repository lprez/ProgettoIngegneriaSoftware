package com.company.busbooking.appcliente;

import com.company.busbooking.dto.CartaDiCreditoDTO;
import com.company.busbooking.interfacce.ServizioCliente;

import java.io.IOException;

public class ComandoVisualizzaCarta extends Comando {
    @Override
    public boolean esegui(long idCliente, ServizioCliente servizioCliente) {
        CartaDiCreditoDTO carta = null;
        try {
            carta = ComandoGestisciCarte.creaMenuCarte(idCliente, servizioCliente).mostraSeleziona();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (carta != null) {
            System.out.println("Codice: " + carta.ottieniCodice());
            System.out.println("Intestatario: " + carta.ottieniIntestatario());
            System.out.println("Scadenza: " + carta.ottieniScadenza());
        }
        return true;
    }

    @Override
    public String descrizione() {
        return "Visualizza carta";
    }
}
