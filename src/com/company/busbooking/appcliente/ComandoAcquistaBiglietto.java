package com.company.busbooking.appcliente;

import com.company.busbooking.dto.CartaDiCreditoDTO;
import com.company.busbooking.dto.AcquistoDTO;
import com.company.busbooking.dto.DescrizioneBigliettoDTO;
import com.company.busbooking.dto.PreferitoDTO;
import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Input;
import com.company.busbooking.util.Menu;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class ComandoAcquistaBiglietto extends Comando {
    @Override
    public boolean esegui(long idCliente, ServizioCliente servizio) {
        String citta;
        Long idDescrizioneBiglietto, idCarta;
        PreferitoDTO preferito = null;

        try {
            citta = chiediCitta(servizio);
            if (citta == null) {
                preferito = chiediPreferito(idCliente, servizio);
                if (preferito == null) return true;
                citta = preferito.ottieniCitta();
                idDescrizioneBiglietto = preferito.ottieniIdDescrizione();
                idCarta = preferito.ottieniIdCarta();
            } else {
                idDescrizioneBiglietto = chiediBiglietto(servizio, citta);
                if (idDescrizioneBiglietto == null) return true;
                idCarta = chiediCarta(idCliente, servizio);
                if (idCarta == null) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }

        int idAcquisto = servizio.acquistaBiglietto(citta, idDescrizioneBiglietto);

        if (idAcquisto >= 0) {
            System.out.println("Creato un nuovo acquisto #" + idAcquisto);
            try {
                if (preferito == null) {
                    creaPreferito(idCliente, citta, idAcquisto, idCarta, servizio);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            paga(idCliente, idCarta, idAcquisto, servizio);
        } else {
            System.out.println("Non è stato possibile creare un nuovo acquisto");
        }

        return true;
    }

    private void creaPreferito(long idCliente, String citta, int idAcquisto, Long idCarta, ServizioCliente servizio) throws IOException {
        System.out.print("Vuoi creare un nuovo preferito? [s/N] ");
        if (Input.leggisN()) {
            servizio.creaPreferito(idCliente, citta, idAcquisto, idCarta);
        }
    }

    private void paga(long idCliente, long idCarta, int idAcquisto, ServizioCliente servizio) {
        System.out.print("Inserisci il codice di sicurezza della carta: ");
        try {
            int codiceSicurezza = Input.leggiIntero();

            if (servizio.effettuaPagamento(idCliente, idAcquisto, idCarta, codiceSicurezza)) {
                System.out.println("Pagamento effettuato con successo.");
            } else {
                System.out.println("Pagamento fallito.");
            }
        } catch (IOException e) {
            servizio.annullaAcquisto(idAcquisto);
            e.printStackTrace();
        }
    }

    private Long chiediBiglietto(ServizioCliente servizio, String citta) throws IOException {
        Collection<DescrizioneBigliettoDTO> listaBiglietti = servizio.richiediCatalogoBiglietti(citta);
        Menu<Long> menuBiglietti = new Menu<Long>(
                listaBiglietti.stream().map(descrizioneBigliettoDTO ->
                        new Menu.ElementoMenu<Long>(
                                descrizioneBigliettoDTO.ottieniDescrizione(),
                                descrizioneBigliettoDTO.ottieniId()
                        )
                ).toList()
        );
        return menuBiglietti.mostraSeleziona();
    }

    private String chiediCitta(ServizioCliente servizio) throws IOException {
        Collection<String> listaCitta = servizio.richiediListaCitta();
        Menu<String> menuCitta = new Menu<String>(
                listaCitta.stream().map(citta -> new Menu.ElementoMenu<String>(citta, citta)).toList(),
                "o lascia vuoto per acquistare dai preferiti"
        );
        return menuCitta.mostraSeleziona();
    }

    private PreferitoDTO chiediPreferito(long idCliente, ServizioCliente servizio) throws IOException {
        Collection<PreferitoDTO> preferiti = servizio.richiediListaPreferiti(idCliente);
        Menu<PreferitoDTO> menuPreferiti = new Menu<PreferitoDTO>(
                preferiti.stream().map(preferito -> new Menu.ElementoMenu<PreferitoDTO>(
                        preferito.ottieniDescrizione(),
                        preferito
                )).collect(Collectors.toList())
        );
        return menuPreferiti.mostraSeleziona();
    }

    private Long chiediCarta(long idCliente, ServizioCliente servizio) throws IOException {
        Collection<CartaDiCreditoDTO> carte;

        while ((carte = servizio.richiediListaCarte(idCliente)).isEmpty()) {
            System.out.println("Non c'è nessuna carta di credito memorizzata. ");
            new ComandoCreaCarta().esegui(idCliente, servizio);
        }

        System.out.println("Seleziona una carta di credito.");
        return new Menu<Long>(
                carte.stream().map(carta -> new Menu.ElementoMenu<Long>(
                        carta.ottieniCodice(),
                        carta.ottieniId()
                )).collect(Collectors.toList())
        ).mostraSeleziona();
    }

    @Override
    public String descrizione() {
        return "Acquista un nuovo biglietto";
    }
}
