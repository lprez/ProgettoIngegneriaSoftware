package com.company.busbooking.appcliente;

import com.company.busbooking.dto.CartaDiCreditoDTO;
import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Input;

import java.io.IOException;

public class ComandoEliminaCarta extends Comando {

    @Override
    public boolean esegui(long idCliente, ServizioCliente servizioCliente) {
        CartaDiCreditoDTO carta = null;
        try {
            carta = ComandoGestisciCarte.creaMenuCarte(idCliente, servizioCliente).mostraSeleziona();
            if (carta != null) {
                System.out.print("Conferma? [s/N] ");
                if (!Input.leggisN()) return true;

                if (servizioCliente.eliminaCarta(idCliente, carta.ottieniId())) {
                    System.out.println("Carta eliminata.");
                } else {
                    System.out.println("Non è stato possibile eliminare la carta.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String descrizione() {
        return "Elimina carta";
    }
}
