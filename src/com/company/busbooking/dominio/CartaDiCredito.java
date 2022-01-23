package com.company.busbooking.dominio;

import com.company.busbooking.util.GeneratoreId;

import java.util.Date;

public class CartaDiCredito {
    private final long id;
    private final String codice, intestatario;
    private final Date scadenza;

    public CartaDiCredito(long id, String codice, String intestatario, Date scadenza) {
        this.id = id;
        this.codice = codice;
        this.intestatario = intestatario;
        this.scadenza = scadenza;
    }

    public CartaDiCredito(String codice, String intestatario, Date scadenza) {
        this(GeneratoreId.ottieniGeneratore().generaIdCarta(), codice, intestatario, scadenza);
    }

    public long ottieniId() {
        return id;
    }

    public String ottieniCodice() {
        return codice;
    }

    public String ottieniIntestatario() {
        return intestatario;
    }

    public Date ottieniScadenza() {
        return scadenza;
    }

    public String ottieniCodiceOscurato() {
        return codice.substring(0, 15) + "XXXX";
    }

    public boolean codiceValido() {
        return codice.matches("[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}");
    }
}
