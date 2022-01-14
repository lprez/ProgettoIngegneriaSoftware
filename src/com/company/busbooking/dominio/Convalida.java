package com.company.busbooking.dominio;

import java.util.Date;

public class Convalida {
    private final Date data;
    private final Corsa corsa;

    public Convalida(Date data, Corsa corsa) {
        this.data = data;
        this.corsa = corsa;
    }

    public Date ottieniData() {
        return data;
    }

    public Corsa ottieniCorsa() {
        return corsa;
    }
}
