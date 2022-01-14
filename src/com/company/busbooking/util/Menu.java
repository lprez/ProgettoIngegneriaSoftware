package com.company.busbooking.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Menu<T> {
    private List<ElementoMenu<T>> dati;

    public Menu() {
        this.dati = Collections.emptyList();
    }

    public Menu(List<ElementoMenu<T>> dati) {
        aggiornaDati(dati);
    }

    public void aggiornaDati(List<ElementoMenu<T>> dati) {
        this.dati = dati;
    }

    public T mostra() throws IOException {
        if (dati.size() < 1) {
            return null;
        } else {
            Iterator<ElementoMenu<T>> iter = this.dati.iterator();
            for (int i = 1; iter.hasNext(); i++) {
                ElementoMenu<T> elementoMenu = iter.next();
                System.out.println(i + ") " + elementoMenu.descrizione);
            }
            System.out.println("Inserisci un numero compreso tra 1 e " + this.dati.size());

            int scelta = Input.leggiIntero();
            if (scelta < 1 || scelta > this.dati.size()) {
                return null;
            } else {
                return this.dati.get(scelta - 1).valore;
            }
        }
    }

    public static class ElementoMenu<T> {
        public final String descrizione;
        public final T valore;

        public ElementoMenu(String descrizione, T valore) {
            this.descrizione = descrizione;
            this.valore = valore;
        }
    }
}
