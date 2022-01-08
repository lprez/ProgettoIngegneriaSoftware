package com.company.busbooking.dominio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AcquistoTest {
    private final DescrizioneBiglietto biglietto1euro =
            new DescrizioneBiglietto(0, BigDecimal.ONE, DescrizioneBiglietto.TipoBiglietto.ANDATA_RITORNO);
    private final CartaDiCredito carta = new CartaDiCredito(0, "", "", new Date());

    /**
     * Verifica che l'importo del pagamento sia uguale a quello del biglietto.
     */
    @Test
    void creaPagamento() {
        Acquisto acquisto = new Acquisto(new Biglietto(biglietto1euro));
        Pagamento pagamento = acquisto.creaPagamento(carta);

        assertEquals(biglietto1euro.ottieniPrezzo(), pagamento.ottieniAmmontare());
        assertEquals(carta.ottieniId(), pagamento.ottieniCartaDiCredito().ottieniId());
    }
}