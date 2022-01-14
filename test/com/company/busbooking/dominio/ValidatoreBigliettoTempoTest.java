package com.company.busbooking.dominio;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidatoreBigliettoTempoTest {
    /**
     * si veda elaborato
     */
    @Test
    void valido() {
        Corsa corsa0 = new Corsa(0);
        Date adesso = new Date(2021, 01, 01, 02, 00),
             unorafa = new Date(2021, 01, 01, 01, 00),
             dueorefa = new Date(2021, 01, 01, 00, 00);
        ValidatoreBiglietto validatore = new ValidatoreBigliettoTempo(90);
        List<Convalida> convalidaUnOraFa = List.of(new Convalida(unorafa, corsa0));
        List<Convalida> convalidaDueOreFa = List.of(new Convalida(dueorefa, corsa0));

        assertTrue(validatore.valido(List.of(), null, adesso));
        assertTrue(validatore.valido(convalidaUnOraFa, null, adesso));
        assertFalse(validatore.valido(convalidaDueOreFa, null, adesso));
    }
}