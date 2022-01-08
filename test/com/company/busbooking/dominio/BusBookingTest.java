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
    }

    /**
     * Verifica che i cataloghi siano stati inseriti correttamente.
     */
    @Test
    void testAggiungiCatalogo() throws BusBooking.CatalogoInesistenteException {
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
    void acquistaBiglietto() throws BusBooking.ErroreCrittografiaException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        DescrizioneBiglietto descrizione = new DescrizioneBiglietto(0, BigDecimal.ONE, DescrizioneBiglietto.TipoBiglietto.TEMPO);

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
        DescrizioneBiglietto descrizione = new DescrizioneBiglietto(1, BigDecimal.ONE, DescrizioneBiglietto.TipoBiglietto.ANDATA_RITORNO);

        assertTrue(busBooking.effettuaPagamento(cliente, acquisto1, cliente.ottieniCarta(0), 111));
        assertFalse(busBooking.effettuaPagamento(cliente, acquisto2, cliente.ottieniCarta(0), 222));

        Iterator<Acquisto> acquisti = cliente.ottieniAcquisti();
        assertEquals(acquisto1, acquisti.next());
        assertFalse(acquisti.hasNext());
    }

}