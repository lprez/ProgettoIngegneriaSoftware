package com.company.busbooking.appcliente;

import com.company.busbooking.dto.DescrizioneBigliettoDTO;
import com.company.busbooking.interfacce.ServizioCliente;
import com.company.busbooking.util.Input;
import com.company.busbooking.util.Menu;

import java.io.IOException;
import java.util.Collection;

public class ComandoAcquistaBiglietto extends Comando {
    @Override
    public boolean esegui(long idCliente, ServizioCliente servizio) {
        String citta;
        Long idDescrizioneBiglietto;

        try {
            citta = chiediCitta(servizio);
            if (citta == null) return true;
            idDescrizioneBiglietto = chiediBiglietto(servizio, citta);
            if (idDescrizioneBiglietto == null) return true;
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }

        int idAcquisto = servizio.acquistaBiglietto(citta, idDescrizioneBiglietto);

        if (idAcquisto >= 0) {
            System.out.println("Creato un nuovo acquisto #" + idAcquisto);
            paga(idCliente, servizio, idAcquisto);
        } else {
            System.out.println("Non è stato possibile creare un nuovo acquisto");
        }

        return true;
    }

    private void paga(long idCliente, ServizioCliente servizio, int idAcquisto) {
        System.out.print("Inserisci il codice di sicurezza della carta: ");
        try {
            int codiceSicurezza = Input.leggiIntero();

            if (servizio.effettuaPagamento(idCliente, idAcquisto, 1, codiceSicurezza)) {
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
        return menuBiglietti.mostra();
    }

    private String chiediCitta(ServizioCliente servizio) throws IOException {
        Collection<String> listaCitta = servizio.richiediListaCitta();
        Menu<String> menuCitta = new Menu<String>(
                listaCitta.stream().map(citta -> new Menu.ElementoMenu<String>(citta, citta)).toList()
        );
        return menuCitta.mostra();
    }

    @Override
    public String descrizione() {
        return "Acquista un nuovo biglietto";
    }
}
