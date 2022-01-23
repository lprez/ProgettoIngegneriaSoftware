package com.company.busbooking.dominio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {
    private final Biglietto biglietto =
            new Biglietto(new DescrizioneBigliettoCorse(0, BigDecimal.ZERO, 1));
    private final CartaDiCredito carta1 = new CartaDiCredito(1, "", "", new Date());
    private final CartaDiCredito carta2 = new CartaDiCredito(2, "", "", new Date());

    /**
     * Questo test verifica che gli acquisti vengano effettivamente aggiunti.
     */
    @Test
    void aggiungiAcquisto() {
        Cliente cliente = new Cliente(0, "");
        Acquisto acquisto = new Acquisto(biglietto);

        assertTrue(cliente.ottieniAcquisti().isEmpty());

        cliente.aggiungiAcquisto(acquisto);
        assertEquals(1,  cliente.ottieniAcquisti().size());

        cliente.aggiungiAcquisto(acquisto);
        assertEquals(2,  cliente.ottieniAcquisti().size());
    }

    /**
     * Questo test verifica che le carte di credito vengano aggiunte e trovate correttamente.
     */
    @Test
    void aggiungiCarta() {
        Cliente cliente = new Cliente(0, "");

        cliente.aggiungiCarta(carta1);
        cliente.aggiungiCarta(carta2);

        assertEquals(carta1.ottieniId(), cliente.ottieniCarta(carta1.ottieniId()).ottieniId());
        assertEquals(carta2.ottieniId(), cliente.ottieniCarta(carta2.ottieniId()).ottieniId());
    }

    @Test
    void creaPreferito() {
        Cliente cliente = new Cliente(0, "");
        Acquisto acquisto = new Acquisto(biglietto);

        cliente.creaPreferito("", acquisto, carta1);
        cliente.creaPreferito("", acquisto, carta2);

        assertEquals(biglietto.ottieniDescrizione().ottieniId(),
                cliente.ottieniPreferiti().get(0).ottieniDescrizioneBiglietto().ottieniId());
        assertEquals(carta1.ottieniId(), cliente.ottieniPreferiti().get(0).ottieniCartaDiCredito().ottieniId());
        assertEquals(carta2.ottieniId(), cliente.ottieniPreferiti().get(1).ottieniCartaDiCredito().ottieniId());
    }
}