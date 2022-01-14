package com.company.busbooking.dominio;

public class Corsa {
    private final long idCorsa;

    public Corsa(long idCorsa) {
        this.idCorsa = idCorsa;
    }

    public long ottieniId() {
        return this.idCorsa;
    }

    @Override
    public boolean equals(Object obj) {
        return idCorsa == ((Corsa) obj).idCorsa;
    }
}
