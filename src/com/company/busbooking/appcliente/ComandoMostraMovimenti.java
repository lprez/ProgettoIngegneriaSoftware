package com.company.busbooking.appcliente;

import com.company.busbooking.dto.AcquistoDTO;
import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Menu;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class ComandoMostraMovimenti extends Comando {
    @Override
    public boolean esegui(long idCliente, ServizioCliente servizio) {
        Collection<AcquistoDTO> movimenti = servizio.richiediElencoMovimenti(idCliente);
        AcquistoDTO scelta;

        Menu<AcquistoDTO> menuMovimenti = new Menu<AcquistoDTO>(
                movimenti.stream().map(acquisto -> new Menu.ElementoMenu<AcquistoDTO>(
                        acquisto.ottieniDescrizione(),
                        acquisto
                )).collect(Collectors.toList())
        );

        try {
            if ((scelta = menuMovimenti.mostraSeleziona()) != null) {
                System.out.println(scelta.ottieniDescrizione());
                System.out.println(scelta.ottieniCarta());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public String descrizione() {
        return "Mostra movimenti";
    }
}
