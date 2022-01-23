package com.company.busbooking.appcliente;

import com.company.busbooking.dto.CartaDiCreditoDTO;
import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Menu;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class ComandoGestisciCarte extends Comando {
    private final List<Comando> comandi = List.of(
            new ComandoCreaCarta(),
            new ComandoVisualizzaCarta(),
            new ComandoModificaCarta(),
            new ComandoEliminaCarta(),
            new ComandoEsci("Torna indietro")
    );

    @Override
    public boolean esegui(long idCliente, ServizioCliente servizioCliente) {
        Menu<CartaDiCreditoDTO> menuCarte = creaMenuCarte(idCliente, servizioCliente);
        menuCarte.mostra(false);

        new MenuComandi(comandi).esegui(idCliente, servizioCliente);
        return true;
    }

    @NotNull
    public static Menu<CartaDiCreditoDTO> creaMenuCarte(long idCliente, ServizioCliente servizioCliente) {
        Collection<CartaDiCreditoDTO> carte = servizioCliente.richiediListaCarte(idCliente);
        Menu<CartaDiCreditoDTO> menuCarte = new Menu<CartaDiCreditoDTO>(
                carte.stream().map(cartaDTO ->
                        new Menu.ElementoMenu<CartaDiCreditoDTO>(
                                cartaDTO.ottieniCodice(),
                                cartaDTO
                        )
                ).toList()
        );
        return menuCarte;
    }

    @Override
    public String descrizione() {
        return "Gestisci carte";
    }
}
