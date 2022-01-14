package com.company.busbooking.interfacce;

import com.company.busbooking.dto.ConvalidaBigliettoDTO;

public interface ServizioAddetto {
    ConvalidaBigliettoDTO convalidaBiglietto(long idAddetto, byte[] codice);
    boolean aggiornaCorsa(long idAddetto, long idCorsa);
}
