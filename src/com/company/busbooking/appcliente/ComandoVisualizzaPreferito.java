package com.company.busbooking.appcliente;

import com.company.busbooking.dto.AcquistoDTO;
import com.company.busbooking.dto.PreferitoDTO;
import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Menu;

import java.io.IOException;

public class ComandoVisualizzaPreferito extends Comando {
    @Override
    public boolean esegui(long idCliente, ServizioCliente servizio) {
        try {
            PreferitoDTO preferito = ComandoGestisciPreferiti.creaMenuPreferiti(idCliente, servizio).mostraSeleziona();
            if (preferito == null) return true;
            System.out.println("Città: " + preferito.ottieniCitta());
            System.out.println(preferito.ottieniDescrizione());
            System.out.println(preferito.ottieniCarta());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String descrizione() {
        return "Visualizza preferito";
    }
}
