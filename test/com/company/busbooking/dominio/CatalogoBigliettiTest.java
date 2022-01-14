package com.company.busbooking.dominio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CatalogoBigliettiTest {
    private final DescrizioneBiglietto descrizione1 = new DescrizioneBigliettoCorse(1, BigDecimal.ZERO, 1);
    private final DescrizioneBiglietto descrizione2 = new DescrizioneBigliettoTempo(2, BigDecimal.ONE, 10);

    /**
     * Questo test verifica che le descrizioni dei biglietti vengano aggiunte e trovate correttamente.
     */
    @Test
    void aggiungiBiglietto() {
        CatalogoBiglietti catalogoBiglietti = new CatalogoBiglietti("");

        catalogoBiglietti.aggiungiBiglietto(descrizione1);
        catalogoBiglietti.aggiungiBiglietto(descrizione2);

        assertEquals(descrizione1.ottieniId(), catalogoBiglietti.ottieniBiglietto(descrizione1.ottieniId()).ottieniId());
        assertEquals(descrizione2.ottieniId(), catalogoBiglietti.ottieniBiglietto(descrizione2.ottieniId()).ottieniId());
    }
}