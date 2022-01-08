package com.company.busbooking.dominio;

import java.math.BigDecimal;

public class Pagamento {
    private final BigDecimal ammontare;
    private final CartaDiCredito pagatoCon;

    private boolean esito = false;
    private ServizioPagamento pagatoAttraverso = null;

    public Pagamento(BigDecimal ammontare, CartaDiCredito carta) {
        this.ammontare = ammontare;
        this.pagatoCon = carta;
    }

    public void impostaServizio(ServizioPagamento servizioPagamento) {
        this.pagatoAttraverso = servizioPagamento;
    }

    public void impostaCompiuto(boolean esito) {
        this.esito = esito;
    }

    public BigDecimal ottieniAmmontare() {
        return ammontare;
    }

    public CartaDiCredito ottieniCartaDiCredito() {
        return pagatoCon;
    }

    public boolean compiuto() {
        return esito;
    }

    public ServizioPagamento ottieniServizio() {
        return pagatoAttraverso;
    }
}
