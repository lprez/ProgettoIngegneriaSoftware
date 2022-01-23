package com.company.busbooking.dominio;

import com.company.busbooking.util.GeneratoreId;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Biglietto {
    private static final String ALGORITMO = "SHA256WithRSA";
    private final long id;
    private final DescrizioneBiglietto descrizione;
    private final List<Convalida> convalide = new LinkedList<>();
    private byte[] firma = null, payload = null;
    private ValidatoreBiglietto validatore = null;

    public Biglietto(DescrizioneBiglietto descrizione) {
        this.id = generaId();
        this.descrizione = descrizione;
    }

    public Biglietto(DescrizioneBiglietto descrizione, byte[] codice) {
        ByteBuffer buffer = ByteBuffer.wrap(codice);
        this.id = buffer.getLong();
        this.firma = new byte[codice.length - Long.SIZE];
        buffer.get(this.firma);
        this.descrizione = descrizione;
    }

    private void aggiungiConvalida(Convalida convalida) {
        this.convalide.add(convalida);
    }

    public void generaFirma(PrivateKey chiave)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature firma = Signature.getInstance(ALGORITMO);
        firma.initSign(chiave);
        firma.update(generaPayload());
        this.firma = firma.sign();
    }

    public boolean autentico(PublicKey chiave)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, BigliettoSenzaFirmaException {
        if (firma == null) {
            throw new BigliettoSenzaFirmaException("Non è possibile verificare l'autenticità senza firma");
        }

        Signature firma = Signature.getInstance(ALGORITMO);
        firma.initVerify(chiave);
        firma.update(generaPayload());
        return firma.verify(ottieniFirma());
    }

    // Per autenticazione offline.
    public static boolean autentico(PublicKey chiave, byte[] codice)
            throws BigliettoSenzaFirmaException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return new Biglietto(null, codice).autentico(chiave);
    }

    // Valido in qualche futura corsa.
    public boolean valido() {
        return this.valido(null);
    }

    // Valido in una specifica corsa.
    public boolean valido(Corsa corsa) {
        return this.ottieniValidatore().valido(this.convalide, corsa);
    }

    // Verifica la validità e registra l'evento.
    public boolean convalida(Corsa corsa) {
        if (!this.valido(corsa)) {
            return false;
        } else if (corsa == null) {
            return true;
        } else if (convalide.stream().filter(convalida -> convalida.ottieniCorsa().equals(corsa)).count() == 0) {
            this.convalide.add(new Convalida(new Date(), corsa));
        }
        return true;
    }

    public byte[] ottieniFirma() {
        return firma;
    }

    public BigDecimal ottieniPrezzo() {
        return descrizione.ottieniPrezzo();
    }

    public ValidatoreBiglietto ottieniValidatore() {
        if (this.validatore == null && this.descrizione != null) {
            return (this.validatore = descrizione.creaValidatore());
        } else {
            return this.validatore;
        }
    }

    public long ottieniId() {
        return id;
    }

    public byte[] ottieniCodice() throws BigliettoSenzaFirmaException {
        if (firma == null)  {
            throw new BigliettoSenzaFirmaException("Non è possibile generare il codice senza firma");
        }

        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE + firma.length);
        buffer.putLong(id);
        buffer.put(firma);
        return buffer.array();
    }

    public static long estraiId(byte[] codice) {
        return new Biglietto(null, codice).ottieniId();
    }

    protected byte[] generaPayload() {
        if (payload == null) {
            ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE);
            buffer.putLong(id);
            return (payload = buffer.array());
        } else {
            return payload;
        }
    }

    private Long generaId() {
        return GeneratoreId.ottieniGeneratore().generaIdBiglietto();
    }

    public DescrizioneBiglietto ottieniDescrizione() {
        return descrizione;
    }

    public class BigliettoSenzaFirmaException extends Exception {
        public BigliettoSenzaFirmaException(String message) {
            super(message);
        }
    }
}
