package com.company.busbooking.dominio;

public class Preferito {
    private final DescrizioneBiglietto descrizioneBiglietto;
    private final CartaDiCredito cartaDiCredito;
    private final String citta;

    public Preferito(String citta, DescrizioneBiglietto descrizioneBiglietto, CartaDiCredito cartaDiCredito) {
        this.descrizioneBiglietto = descrizioneBiglietto;
        this.cartaDiCredito = cartaDiCredito;
        this.citta = citta;
    }

    public DescrizioneBiglietto ottieniDescrizioneBiglietto() {
        return descrizioneBiglietto;
    }

    public CartaDiCredito ottieniCartaDiCredito() {
        return cartaDiCredito;
    }

    public String ottieniCitta() { return citta; }
}
