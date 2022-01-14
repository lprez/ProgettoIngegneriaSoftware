package com.company.busbooking.dominio;

import com.company.busbooking.dominio.util.GeneratoreChiavi;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.*;

import static org.junit.jupiter.api.Assertions.*;

class BigliettoTest {
    private final DescrizioneBiglietto descrizione1corsa = new DescrizioneBigliettoCorse(0, BigDecimal.ONE, 1);
    private final DescrizioneBiglietto descrizione2corse = new DescrizioneBigliettoCorse(1, BigDecimal.ONE, 2);
    private final KeyPair chiave;

    BigliettoTest() throws NoSuchAlgorithmException {
        this.chiave = GeneratoreChiavi.generaChiavi();
    }

    /**
     * Verifica che i biglietti generati e verificati con la stessa coppia di chiavi siano considerati autentici.
     */
    @Test
    void generaCodiceAutentico() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, Biglietto.BigliettoSenzaFirmaException {
        Biglietto biglietto1 = new Biglietto(descrizione1corsa);
        biglietto1.generaFirma(chiave.getPrivate());

        Biglietto biglietto2 = new Biglietto(descrizione1corsa, biglietto1.ottieniCodice());
        assertTrue(biglietto2.autentico(chiave.getPublic()));
    }

    /**
     * Verifica che la convalida venga registrata se necessario.
     */
    @Test
    void convalida() {
        Corsa corsa1 = new Corsa(1), corsa2 = new Corsa(2);
        Biglietto biglietto1 = new Biglietto(descrizione1corsa);

        assertTrue(biglietto1.convalida(null));

        assertTrue(biglietto1.convalida(corsa1));
        // La convalida non va registrata se l'ultima corsa è uguale.
        assertTrue(biglietto1.convalida(corsa1));
        assertTrue(biglietto1.convalida(corsa1));

        // Se la convalida è stata registrata abbiamo esaurito le corse.
        assertFalse(biglietto1.convalida(corsa2));
        assertFalse(biglietto1.convalida(corsa2));

        Biglietto biglietto2 = new Biglietto(descrizione2corse);

        assertTrue(biglietto2.convalida(null));

        assertTrue(biglietto2.convalida(corsa1));
        assertTrue(biglietto2.convalida(corsa1));

        // In questo caso abbiamo ancora 1 corsa.
        assertTrue(biglietto2.convalida(corsa2));
        assertTrue(biglietto2.convalida(corsa2));
        // Non possiamo nuovamente fare la corsa di andata se abbiamo già iniziato a tornare.
        assertFalse(biglietto2.convalida(corsa1));
    }

    /**
     * Verifica che venga correttamente generato un codice con ID e firma, verificando
     * tali proprietà su un nuovo biglietto creato a partire dal codice.
     */
    @Test
    void ottieniCodice() throws Biglietto.BigliettoSenzaFirmaException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Biglietto biglietto = new Biglietto(descrizione1corsa);
        biglietto.generaFirma(chiave.getPrivate());
        Biglietto biglietto2 = new Biglietto(descrizione1corsa, biglietto.ottieniCodice());
        assertEquals(biglietto.ottieniId(), biglietto2.ottieniId());
        assertTrue(biglietto2.autentico(chiave.getPublic()));
        assertArrayEquals(biglietto.ottieniCodice(), biglietto2.ottieniCodice());
    }

    @Test
    void estraiId() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, Biglietto.BigliettoSenzaFirmaException {
        Biglietto biglietto = new Biglietto(descrizione1corsa);
        biglietto.generaFirma(chiave.getPrivate());
        assertEquals(biglietto.ottieniId(), Biglietto.estraiId(biglietto.ottieniCodice()));
    }
}