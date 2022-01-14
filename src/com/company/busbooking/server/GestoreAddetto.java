package com.company.busbooking.server;

import com.company.busbooking.dominio.BusBooking;
import com.company.busbooking.dto.ConvalidaBigliettoDTO;
import com.company.busbooking.interfacce.ServizioAddetto;

public class GestoreAddetto implements ServizioAddetto {
    private final BusBooking busBooking;

    public GestoreAddetto(BusBooking busBooking) {
        this.busBooking = busBooking;
    }

    @Override
    public ConvalidaBigliettoDTO convalidaBiglietto(long idAddetto, byte[] codice) {
        try {
            return new ConvalidaBigliettoDTO(
                    busBooking.convalidaBiglietto(busBooking.ottieniAddetto(idAddetto), codice)
            );
        } catch (BusBooking.BigliettoInesistenteException e) {
            return new ConvalidaBigliettoDTO(false);
        } catch (BusBooking.AddettoInesistenteException e) {
            return new ConvalidaBigliettoDTO(e.getMessage());
        }
    }

    @Override
    public boolean aggiornaCorsa(long idAddetto, long idCorsa) {
        try {
            busBooking.aggiornaCorsa(
                    busBooking.ottieniAddetto(idAddetto),
                    idCorsa == -1 ? null : busBooking.ottieniCorsa(idCorsa)
            );
            return true;
        } catch (BusBooking.AddettoInesistenteException | BusBooking.CorsaInesistenteException e) {
            e.printStackTrace();
            return false;
        }
    }
}
