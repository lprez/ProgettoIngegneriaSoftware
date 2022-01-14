package com.company.busbooking.appcliente;

import com.company.busbooking.dto.BigliettoDTO;
import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Menu;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;

public class ComandoMostraBiglietto extends Comando {
    @Override
    public boolean esegui(long idCliente, ServizioCliente servizioCliente) {
        Integer indiceBiglietto = menuListaBiglietti(idCliente, servizioCliente);

        if (indiceBiglietto != null) {
            byte[] codice = servizioCliente.richiediCodice(idCliente, indiceBiglietto);
            try {
                System.out.println("Codice: " + new ObjectMapper().writeValueAsString(codice));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Nullable
    private Integer menuListaBiglietti(long idCliente, ServizioCliente servizioCliente) {
        Collection<BigliettoDTO> biglietti = servizioCliente.richiediListaBiglietti(idCliente);
        Menu<Integer> menuBiglietti = new Menu<Integer>(
                biglietti.stream().map(bigliettoDTO ->
                        new Menu.ElementoMenu<Integer>(
                                bigliettoDTO.ottieniDescrizione(),
                                bigliettoDTO.ottieniIndice()
                        )
                ).toList()
        );

        Integer indiceBiglietto = null;
        try {
            indiceBiglietto = menuBiglietti.mostra();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return indiceBiglietto;
    }

    @Override
    public String descrizione() {
        return "Mostra biglietto";
    }
}
