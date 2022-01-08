package com.company.busbooking.dominio;

import java.util.HashMap;
import java.util.Iterator;

public class CatalogoBiglietti implements Iterable<DescrizioneBiglietto> {
    private final String citta;
    private final HashMap<Long, DescrizioneBiglietto> biglietti = new HashMap<Long, DescrizioneBiglietto>();

    public CatalogoBiglietti(String citta) {
        this.citta = citta;
    }

    public void aggiungiBiglietto(DescrizioneBiglietto biglietto) {
        this.biglietti.put(biglietto.ottieniId(), biglietto);
    }

    public DescrizioneBiglietto ottieniBiglietto(long id) {
        return this.biglietti.get(id);
    }

    public String ottieniCitta() {
        return citta;
    }

    @Override
    public Iterator<DescrizioneBiglietto> iterator() {
        return biglietti.values().iterator();
    }
}
