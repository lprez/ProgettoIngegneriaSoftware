package com.company.busbooking.interfacce;

import com.company.busbooking.dto.BigliettoDTO;
import com.company.busbooking.dto.DescrizioneBigliettoDTO;

import java.util.Collection;

public interface ServizioCliente {
    Collection<String> richiediListaCitta();
    Collection<DescrizioneBigliettoDTO> richiediCatalogoBiglietti(String citta);
    int acquistaBiglietto(String citta, long idDescrizione);
    boolean effettuaPagamento(long idCliente, int idAcquisto, long idCartaCredito, int codiceSicurezza);
    void annullaAcquisto(int idAcquisto);
    Collection<BigliettoDTO> richiediListaBiglietti(long idCliente);
    byte[] richiediCodice(long idCliente, int indiceBiglietto);
}
