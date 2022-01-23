package com.company.busbooking.dominio;

import java.util.*;

public class Cliente {
    private final long id;
    private final String nome;
    private final Map<Long, CartaDiCredito> carte = new HashMap<>();
    private final List<Acquisto> acquisti = new LinkedList<>();
    private final List<Preferito> preferiti = new LinkedList<>();

    public Cliente(long id, String nome) {
        this.nome = nome;
        this.id = id;
    }

    public void aggiungiAcquisto(Acquisto acquisto) {
        acquisti.add(acquisto);
    }

    public List<Acquisto> ottieniAcquisti() {
        return acquisti;
    }

    public List<Biglietto> ottieniListaBiglietti() {
        List<Biglietto> listaBiglietti = new ArrayList<>();

        for (Iterator<Acquisto> iter = acquisti.iterator(); iter.hasNext();) {
            Biglietto biglietto = iter.next().ottieniBiglietto();

            if (biglietto.valido()) {
                listaBiglietti.add(biglietto);
            }
        }

        return listaBiglietti;
    }

    public void creaPreferito(String citta, Acquisto acquisto, CartaDiCredito carta) {
        DescrizioneBiglietto descrizione = acquisto.ottieniBiglietto().ottieniDescrizione();
        Preferito preferito = new Preferito(citta, descrizione, carta);
        this.aggiungiPreferito(preferito);
    }

    private void aggiungiPreferito(Preferito preferito) {
        this.preferiti.add(preferito);
    }

    public List<Preferito> ottieniPreferiti() {
        return this.preferiti;
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

    public Collection<CartaDiCredito> ottieniListaCarte() {
        return carte.values();
    }

    public void eliminaPreferito(int indice) {
        if (indice < this.preferiti.size()) {
            this.preferiti.remove(indice);
        }
    }

    public boolean eliminaCarta(long idCarta) {
        if (this.carte.containsKey(idCarta)) {
            this.carte.remove(idCarta);
            return true;
        } else {
            return false;
        }
    }
}
