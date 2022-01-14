package com.company.busbooking.dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddettoTest {

    @Test
    void aggiornaCorsa() {
        Addetto addetto = new Addetto(0, "");
        Corsa corsa = new Corsa(0);

        addetto.aggiornaCorsa(corsa);
        assertEquals(corsa.ottieniId(), addetto.ottieniCorsa().ottieniId());

        addetto.aggiornaCorsa(null);
        assertNull(addetto.ottieniCorsa());
    }
}