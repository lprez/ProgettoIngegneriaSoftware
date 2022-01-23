package com.company.busbooking.util;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Menu<T> {
    private List<ElementoMenu<T>> dati;
    private final String alternativa;

    public Menu() {
        this(Collections.emptyList());
    }

    public Menu(List<ElementoMenu<T>> dati) {
        this(dati, null);
    }

    public Menu(List<ElementoMenu<T>> dati, String alternativa) {
        aggiornaDati(dati);
        this.alternativa = alternativa;
    }

    public void aggiornaDati(List<ElementoMenu<T>> dati) {
        this.dati = dati;
    }

    public T mostraSeleziona() throws IOException {
        mostra(true);
        return seleziona();
    }

    public Integer mostraSelezionaIndice() throws IOException {
        mostra(true);
        return selezionaIndice();
    }

    public Integer selezionaIndice() throws IOException {
        if (this.dati.size() < 1) {
            return null;
        } else {
            if (this.alternativa == null) {
                System.out.println("Inserisci un numero compreso tra 1 e " + this.dati.size());
            } else {
                System.out.println("Inserisci un numero compreso tra 1 e " + this.dati.size() + ", " + this.alternativa);
            }

            Integer scelta = Input.leggiIntero();
            if (scelta == null || scelta < 1 || scelta > this.dati.size()) {
                return null;
            } else {
                return scelta - 1;
            }
        }
    }

    @Nullable
    public T seleziona() throws IOException {
        Integer indice = selezionaIndice();
        return indice == null ? null : this.dati.get(indice).valore;
    }

    public void mostra(boolean numerato) {
        Iterator<ElementoMenu<T>> iter = this.dati.iterator();
        for (int i = 1; iter.hasNext(); i++) {
            ElementoMenu<T> elementoMenu = iter.next();
            if (numerato) {
                System.out.println(i + ") " + elementoMenu.descrizione);
            } else {
                System.out.println("- " + elementoMenu.descrizione);
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
