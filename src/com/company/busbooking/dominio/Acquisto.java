package com.company.busbooking.dominio;

import java.math.BigDecimal;
import java.util.Date;

public class Acquisto {
    private final Date date;
    private final BigDecimal importo;
    private final Biglietto biglietto;
    private Pagamento pagatoDa;

    public Acquisto(Biglietto biglietto) {
        this.biglietto = biglietto;
        this.date = new Date();
        this.importo = biglietto.ottieniPrezzo();
    }

    public Pagamento creaPagamento(CartaDiCredito cartaDiCredito) {
        return (this.pagatoDa = new Pagamento(ottieniImporto(), cartaDiCredito));
    }

    public BigDecimal ottieniImporto() {
        return importo;
    }

    public Pagamento ottieniPagamento() {
        return pagatoDa;
    }

    public Biglietto ottieniBiglietto() {
        return this.biglietto;
    }
}
