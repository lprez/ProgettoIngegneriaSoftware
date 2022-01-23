package com.company.busbooking.appcliente;

import com.company.busbooking.dto.AcquistoDTO;
import com.company.busbooking.dto.PreferitoDTO;
import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Menu;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ComandoGestisciPreferiti extends Comando {

    @Override
    public boolean esegui(long idCliente, ServizioCliente servizio) {
        Menu<PreferitoDTO> menuPreferiti = creaMenuPreferiti(idCliente, servizio);
        menuPreferiti.mostra(false);

        List<Comando> comandi = List.of(
                new ComandoVisualizzaPreferito(),
                new ComandoEliminaPreferito(),
                new ComandoEsci("Torna indietro")
        );
        new MenuComandi(comandi).esegui(idCliente, servizio);

        return true;
    }

    @NotNull
    public static Menu<PreferitoDTO> creaMenuPreferiti(long idCliente, ServizioCliente servizio) {
        Collection<PreferitoDTO> preferiti = servizio.richiediListaPreferiti(idCliente);
        return new Menu<PreferitoDTO>(
                preferiti.stream().map(preferito -> new Menu.ElementoMenu<PreferitoDTO>(
                        preferito.ottieniDescrizione(),
                        preferito
                )).collect(Collectors.toList())
        );
    }

    @Override
    public String descrizione() {
        return "Gestisci preferiti";
    }
}
