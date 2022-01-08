package com.company.busbooking.dominio;

public abstract class ServizioPagamento {
    private final String nome;
    private final ServizioPagamento prossimo;

    public ServizioPagamento(String nome, ServizioPagamento prossimo) {
        this.nome = nome;
        this.prossimo = prossimo;
    }

    public ServizioPagamento(String nome) {
        this(nome, null);
    }

    public boolean paga(Pagamento pagamento, int codiceSicurezza) {
        if (pagaTentativo(pagamento, codiceSicurezza)) {
            pagamento.impostaServizio(this);
            pagamento.impostaCompiuto(true);
            return true;
        } else if (prossimo != null) {
            return prossimo.paga(pagamento, codiceSicurezza);
        } else {
            pagamento.impostaCompiuto(false);
            return false;
        }
    }

    protected abstract boolean pagaTentativo(Pagamento pagamento, int codice);
}
