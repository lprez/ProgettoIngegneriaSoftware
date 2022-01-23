package com.company.busbooking.dominio;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CartaDiCreditoTest {

    @Test
    void ottieniCodiceOscurato() {
        CartaDiCredito carta = new CartaDiCredito(0, "1234-5678-9012-3456", "", new Date());
        assertEquals("1234-5678-9012-XXXX", carta.ottieniCodiceOscurato());
    }

    @Test
    void codiceValido() {
        CartaDiCredito carta1 = new CartaDiCredito(0, "1234-5678-9012-3456", "", new Date());
        CartaDiCredito carta2 = new CartaDiCredito(0, "1234-5678-9012", "", new Date());
        CartaDiCredito carta3 = new CartaDiCredito(0, "1234-5678-9012-3456-7890", "", new Date());
        CartaDiCredito carta4 = new CartaDiCredito(0, "1234567890123456", "", new Date());

        assertTrue(carta1.codiceValido());
        assertFalse(carta2.codiceValido());
        assertFalse(carta3.codiceValido());
        assertFalse(carta4.codiceValido());
    }
}