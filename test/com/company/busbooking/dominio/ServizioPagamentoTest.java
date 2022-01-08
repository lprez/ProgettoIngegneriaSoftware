package com.company.busbooking.dominio;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ServizioPagamentoTest {
    private final CartaDiCredito carta = new CartaDiCredito(0, "", "", new Date());

    /**
     * Questo test verifica il corretto funzionamento della catena di
     * servizi di pagamento a prescindere dalla specifica implementazione di
     * ciasciuno di essi. I valori della carta di credito e dell'importo sono
     * pertanto irrilevanti, le condizioni in ingresso che contano per il
     * metodo paga sono la disponibilità del servizio attuale e quella del
     * resto della catena.
     */
    @Test
    void paga() {
        ServizioPagamento servizio1 = new ServizioPagamentoStub("Servizio1", false);
        ServizioPagamento servizio2 = new ServizioPagamentoStub("Servizio2", true);
        ServizioPagamento servizio3 = new ServizioPagamentoStub("Servizio3", servizio2, false);
        ServizioPagamento servizio4 = new ServizioPagamentoStub("Servizio4", servizio2, true);

        asserzionePagamento(servizio1, false, null, "Indisponibile -> Indisponibile");
        asserzionePagamento(servizio2, true, servizio2, "Disponibile -> Indisponibile");
        asserzionePagamento(servizio3, true, servizio2, "Indisponibile -> Disponibile");
        asserzionePagamento(servizio4, true, servizio4, "Disponibile -> Disponibile");
    }

    private void asserzionePagamento(ServizioPagamento testa, boolean esitoAtteso, ServizioPagamento servizioAtteso, String messaggio) {
        Pagamento pagamento = new Pagamento(BigDecimal.valueOf(100), carta);

        assertEquals(esitoAtteso, testa.paga(pagamento, 0), messaggio);
        assertEquals(esitoAtteso, pagamento.compiuto(), messaggio);
        assertEquals(servizioAtteso, pagamento.ottieniServizio(), messaggio);
    }

    private class ServizioPagamentoStub extends ServizioPagamento {
        private boolean disponibile = false;

        public ServizioPagamentoStub(String nome, ServizioPagamento prossimo, boolean disponibile) {
            super(nome, prossimo);
            this.disponibile = disponibile;
        }

        public ServizioPagamentoStub(String nome, boolean disponibile) {
            super(nome);
            this.disponibile = disponibile;
        }

        @Override
        protected boolean pagaTentativo(Pagamento pagamento, int codice) {
            return disponibile;
        }
    }
}