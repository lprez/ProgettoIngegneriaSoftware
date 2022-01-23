package com.company.busbooking.interfacce;

import com.company.busbooking.dto.*;

import java.util.Collection;

public interface ServizioCliente {
    Collection<String> richiediListaCitta();
    Collection<DescrizioneBigliettoDTO> richiediCatalogoBiglietti(String citta);
    int acquistaBiglietto(String citta, long idDescrizione);
    boolean effettuaPagamento(long idCliente, int idAcquisto, long idCarta, int codiceSicurezza);
    void annullaAcquisto(int idAcquisto);
    Collection<BigliettoDTO> richiediListaBiglietti(long idCliente);
    byte[] richiediCodice(long idCliente, int indiceBiglietto);
    boolean creaPreferito(long idCliente, String citta, int idAcquisto, long idCarta);
    boolean eliminaPreferito(long idCliente, int indicePreferito);
    Collection<PreferitoDTO> richiediListaPreferiti(long idCliente);
    Collection<CartaDiCreditoDTO> richiediListaCarte(long idCliente);
    boolean creaCarta(long idCliente, String codice, String intestatario, String scadenza);
    boolean modificaCarta(long idCliente, long idCarta, String codice, String intestatario, String scadenza);
    boolean eliminaCarta(long idCliente, long idCarta);
    Collection<AcquistoDTO> richiediElencoMovimenti(long idCliente);
}
