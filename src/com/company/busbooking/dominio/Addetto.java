package com.company.busbooking.dominio;

public class Addetto {
    private final long idAddetto;
    private final String nome;
    private Corsa corsa = null;

    public Addetto(long idAddetto, String nome) {
        this.idAddetto = idAddetto;
        this.nome = nome;
    }

    public Corsa ottieniCorsa() {
        return corsa;
    }

    public void aggiornaCorsa(Corsa corsa) {
        this.corsa = corsa;
    }

    public long ottieniId() {
        return idAddetto;
    }
}
