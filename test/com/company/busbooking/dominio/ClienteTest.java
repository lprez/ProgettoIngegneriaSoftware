package com.company.busbooking.dominio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {
    private final Biglietto biglietto =
            new Biglietto(new DescrizioneBiglietto(0, BigDecimal.ZERO, DescrizioneBiglietto.TipoBiglietto.ANDATA_RITORNO));
    private final CartaDiCredito carta1 = new CartaDiCredito(1, "", "", new Date());
    private final CartaDiCredito carta2 = new CartaDiCredito(2, "", "", new Date());

    /**
     * Questo test verifica che gli acquisti vengano effettivamente aggiunti.
     */
    @Test
    void aggiungiAcquisto() {
        Cliente cliente = new Cliente(0, "");
        Acquisto acquisto = new Acquisto(biglietto);

        assertFalse(cliente.ottieniAcquisti().hasNext());

        cliente.aggiungiAcquisto(acquisto);
        Iterator<Acquisto> it = cliente.ottieniAcquisti();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());

        cliente.aggiungiAcquisto(acquisto);
        it = cliente.ottieniAcquisti();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
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
}