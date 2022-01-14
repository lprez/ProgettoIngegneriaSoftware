package com.company.busbooking.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

public class ConvalidaBigliettoDTO {
    private final boolean valido;
    private final boolean errore;
    private final String messaggio;

    public ConvalidaBigliettoDTO(boolean valido, boolean errore, String messaggio) {
        this.valido = valido;
        this.errore = errore;
        this.messaggio = messaggio;
    }

    public ConvalidaBigliettoDTO(boolean valido) {
        this(valido, false, "");
    }

    public ConvalidaBigliettoDTO(String messaggioErrore) {
        this(false, true, messaggioErrore);
    }

    @JsonGetter("valido")
    public boolean ottieniValido() {
        return valido;
    }

    @JsonGetter("errore")
    public boolean ottieniErrore() {
        return errore;
    }

    @JsonGetter("messaggio")
    public String ottieniMessaggio() {
        return messaggio;
    }
}
