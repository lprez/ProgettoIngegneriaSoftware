package com.company.busbooking.dominio;

import com.company.busbooking.dominio.util.GeneratoreChiavi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BusBookingTest {
    private final ServizioPagamento servizioPagamento = new ServizioPagamento("", null) {
        @Override
        protected boolean pagaTentativo(Pagamento pagamento, int codice) {
            return codice == 111;
        }
    };
    private BusBooking busBooking;
    private Acquisto acquisto1, acquisto2;

    @BeforeAll
    void inizializza() throws NoSuchAlgorithmException {
        busBooking = new BusBooking(GeneratoreChiavi.generaChiavi(), servizioPagamento);

        CatalogoBiglietti catalogo1 = new CatalogoBiglietti("Catania");
        CatalogoBiglietti catalogo2 = new CatalogoBiglietti("Palermo");
        busBooking.aggiungiCatalogo(catalogo1);
        busBooking.aggiungiCatalogo(catalogo2);

        CartaDiCredito carta1 = new CartaDiCredito(0, "0000-0000-0000-0000", "Cliente 1", new Date());
        CartaDiCredito carta2 = new CartaDiCredito(1, "1111-1111-1111-1111", "Cliente 2", new Date());

        Cliente cliente1 = new Cliente(0, "Cliente 1");
        cliente1.aggiungiCarta(carta1);

        Cliente cliente2 = new Cliente(1, "Cliente 2");
        cliente2.aggiungiCarta(carta2);

        busBooking.aggiungiCliente(cliente1);
        busBooking.aggiungiCliente(cliente2);

        Addetto addetto1 = new Addetto(1, "Pippo"), addetto2 = new Addetto(2, "");
        Corsa corsa1 = new Corsa(1), corsa2 = new Corsa(2);
        addetto1.aggiornaCorsa(corsa1);
        addetto2.aggiornaCorsa(corsa2);

        busBooking.aggiungiAddetto(addetto1);
        busBooking.aggiungiAddetto(addetto2);
        busBooking.aggiungiCorsa(corsa1);
        busBooking.aggiungiCorsa(corsa2);
    }

    /**
     * Verifica che i cataloghi siano stati inseriti correttamente.
     */
    @Test
    void aggiungiCatalogo() throws BusBooking.CatalogoInesistenteException {
        HashSet<String> listaCitta = new HashSet<>(busBooking.ottieniListaCitta());
        assertTrue(listaCitta.contains("Palermo"));
        assertTrue(listaCitta.contains("Catania"));

        assertEquals("Palermo", busBooking.ottieniCatalogo("Palermo").ottieniCitta());
        assertEquals("Catania", busBooking.ottieniCatalogo("Catania").ottieniCitta());

        assertThrows(BusBooking.CatalogoInesistenteException.class, () -> busBooking.ottieniCatalogo(""));
    }

    @Test
    void aggiungiCliente() throws BusBooking.ClienteInesistenteException {
        assertEquals(0, busBooking.ottieniCliente(0).ottieniId());
        assertEquals(1, busBooking.ottieniCliente(1).ottieniId());

        assertThrows(BusBooking.ClienteInesistenteException.class, () -> busBooking.ottieniCliente(2));
    }

    /**
     * Verifica che i biglietti in acquisto siano autentici.
     */
    @Test
    void acquistaBiglietto() throws BusBooking.ErroreCrittografiaException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, Biglietto.BigliettoSenzaFirmaException {
        DescrizioneBiglietto descrizione = new DescrizioneBigliettoTempo(0, BigDecimal.ONE, 10);

        acquisto1 = busBooking.acquistaBiglietto(descrizione);
        acquisto2 = busBooking.acquistaBiglietto(descrizione);

        assertTrue(acquisto1.ottieniBiglietto().autentico(busBooking.ottieniChiavi().getPublic()));
        assertTrue(acquisto2.ottieniBiglietto().autentico(busBooking.ottieniChiavi().getPublic()));
    }

    /**
     * Verifica che il cliente abbia registrato tutti e soli gli acquisti pagati con successo,
     */
    @Test
    void effettuaPagamento() throws BusBooking.ErroreCrittografiaException, BusBooking.ClienteInesistenteException {
        Cliente cliente = busBooking.ottieniCliente(0);

        assertTrue(busBooking.effettuaPagamento(cliente, acquisto1, cliente.ottieniCarta(0), 111));
        assertFalse(busBooking.effettuaPagamento(cliente, acquisto2, cliente.ottieniCarta(0), 222));

        List<Acquisto> acquisti = cliente.ottieniAcquisti();
        assertEquals(acquisto1, acquisti.get(0));
        assertEquals(1, acquisti.size());
    }

    @Test
    void aggiungiAddetto() throws BusBooking.AddettoInesistenteException {
        assertEquals(1, busBooking.ottieniAddetto(1).ottieniId());
        assertEquals(2, busBooking.ottieniAddetto(2).ottieniId());

        assertThrows(BusBooking.AddettoInesistenteException.class, () -> busBooking.ottieniAddetto(3));
    }

    @Test
    void aggiungiCorsa() throws BusBooking.CorsaInesistenteException {
        assertEquals(1, busBooking.ottieniCorsa(1).ottieniId());
        assertEquals(2, busBooking.ottieniCorsa(2).ottieniId());

        assertThrows(BusBooking.CorsaInesistenteException.class, () -> busBooking.ottieniCorsa(3));
    }

    @Test
    void aggiornaCorsa() throws BusBooking.AddettoInesistenteException {
        assertEquals(1, busBooking.ottieniAddetto(1).ottieniCorsa().ottieniId());
        assertEquals(2, busBooking.ottieniAddetto(2).ottieniCorsa().ottieniId());
    }
}