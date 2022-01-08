package com.company.busbooking.dominio;

import com.company.busbooking.dominio.util.GeneratoreChiavi;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.*;

import static org.junit.jupiter.api.Assertions.*;

class BigliettoTest {
    private final DescrizioneBiglietto descrizione =
            new DescrizioneBiglietto(0, BigDecimal.ZERO, DescrizioneBiglietto.TipoBiglietto.ANDATA_RITORNO);

    /**
     * Verifica che i biglietti generati e verificati con la stessa coppia di chiavi siano considerati autentici.
     */
    @Test
    void generaCodiceAutentico() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        KeyPair chiave = GeneratoreChiavi.generaChiavi();

        Biglietto biglietto1 = new Biglietto(descrizione);
        biglietto1.generaFirma(chiave.getPrivate());

        Biglietto biglietto2 = new Biglietto(descrizione, biglietto1.ottieniId(), biglietto1.ottieniFirma());
        assertTrue(biglietto2.autentico(chiave.getPublic()));
    }
}