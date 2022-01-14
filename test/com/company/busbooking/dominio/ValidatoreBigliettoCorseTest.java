package com.company.busbooking.dominio;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidatoreBigliettoCorseTest {
    /**
     * si veda elaborato
     */
    @Test
    void valido2corse() {
        Corsa corsa0 = new Corsa(0), corsa1 = new Corsa(1);
        Date oggi = new Date(2021, 01, 02), ieri = new Date(2021, 01, 01);
        ValidatoreBiglietto validatore = new ValidatoreBigliettoCorse(2);
        List<Convalida> convalidaIeri0 = List.of(new Convalida(ieri, corsa0));
        List<Convalida> convalidaOggi0 = List.of(new Convalida(oggi, corsa0));
        List<Convalida> convalideOggi01 = List.of(new Convalida(oggi, corsa0), new Convalida(oggi, corsa1));

        assertTrue(validatore.valido(List.of(), null, oggi));
        assertFalse(validatore.valido(convalidaIeri0, null, oggi));
        assertTrue(validatore.valido(convalidaOggi0, null, oggi));
        assertTrue(validatore.valido(convalidaOggi0, corsa0, oggi));
        assertFalse(validatore.valido(convalideOggi01, corsa0, oggi));
        assertTrue(validatore.valido(convalidaOggi0, corsa1, oggi));
    }

    @Test
    void valido1corsa() {
        Corsa corsa0 = new Corsa(0), corsa1 = new Corsa(1);
        Date oggi = new Date(2021, 01, 02), ieri = new Date(2021, 01, 01);
        ValidatoreBiglietto validatore = new ValidatoreBigliettoCorse(1);
        List<Convalida> convalidaIeri0 = List.of(new Convalida(ieri, corsa0));
        List<Convalida> convalidaOggi0 = List.of(new Convalida(oggi, corsa0));

        assertTrue(validatore.valido(List.of(), null, oggi));
        assertFalse(validatore.valido(convalidaIeri0, null, oggi));
        assertTrue(validatore.valido(convalidaOggi0, null, oggi));
        assertTrue(validatore.valido(convalidaOggi0, corsa0, oggi));
        assertFalse(validatore.valido(convalidaOggi0, corsa1, oggi));
    }
}