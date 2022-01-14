package com.company.busbooking.appaddetto;

import com.company.busbooking.dominio.Biglietto;
import com.company.busbooking.dto.ConvalidaBigliettoDTO;
import com.company.busbooking.interfacce.ServizioAddetto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;

public class ServizioAddettoProxyOffline implements ServizioAddetto {
    private final PublicKey chiave;

    public ServizioAddettoProxyOffline(PublicKey chiave) {
        this.chiave = chiave;
    }

    @Override
    public ConvalidaBigliettoDTO convalidaBiglietto(long idAddetto, byte[] codice) {
        try {
            if (Biglietto.autentico(chiave, codice)) {
                return new ConvalidaBigliettoDTO(true, true, "Non è stato possibile verificare la validità del biglietto, tuttavia esso è autentico.");
            } else {
                return new ConvalidaBigliettoDTO(false);
            }
        } catch (Biglietto.BigliettoSenzaFirmaException | NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            return new ConvalidaBigliettoDTO(e.getMessage());
        }
    }

    @Override
    public boolean aggiornaCorsa(long idAddetto, long idCorsa) {
        return false;
    }
}
