package com.company.busbooking.dominio;

import java.util.*;

public class Cliente {
    private final long id;
    private String nome;
    private final Map<Long, CartaDiCredito> carte = new HashMap<>();
    private final List<Acquisto> acquisti = new LinkedList<>();

    public Cliente(long id, String nome) {
        this.nome = nome;
        this.id = id;
    }

    public void aggiungiAcquisto(Acquisto acquisto) {
        acquisti.add(acquisto);
    }

    public Iterator<Acquisto> ottieniAcquisti() {
        return acquisti.iterator();
    }

    public String ottieniNome() {
        return nome;
    }

    public long ottieniId() { return id; }

    public void aggiungiCarta(CartaDiCredito carta) {
        carte.put(carta.ottieniId(), carta);
    }

    public CartaDiCredito ottieniCarta(long idCartaCredito) {
        return carte.get(idCartaCredito);
    }
}
