package com.company.busbooking.util;

public class GeneratoreId {
    private static GeneratoreId istanza;
    private long idBiglietto = 0;

    private GeneratoreId() {}

    public static GeneratoreId ottieniGeneratore() {
        if (istanza == null) {
            istanza = new GeneratoreId();
        }
        return istanza;
    }

    public long generaIdBiglietto() {
        return idBiglietto++;
    }
}
