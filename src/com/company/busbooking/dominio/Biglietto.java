package com.company.busbooking.dominio;

import com.company.busbooking.util.GeneratoreId;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.security.*;

public class Biglietto {
    private static final String ALGORITMO = "SHA256WithRSA";
    private final long id;
    private final DescrizioneBiglietto descrizione;
    private byte[] firma = null, payload = null;

    public Biglietto(DescrizioneBiglietto descrizione) {
        this.id = generaId();
        this.descrizione = descrizione;
    }

    public Biglietto(DescrizioneBiglietto descrizione, long id, byte[] firma) {
        this.id = id;
        this.descrizione = descrizione;
        this.firma = firma;
    }

    public void generaFirma(PrivateKey chiave)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature firma = Signature.getInstance(ALGORITMO);
        firma.initSign(chiave);
        firma.update(generaPayload());
        this.firma = firma.sign();
    }

    public boolean autentico(PublicKey chiave)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (firma == null) {
            return false;
        }

        Signature firma = Signature.getInstance(ALGORITMO);
        firma.initVerify(chiave);
        firma.update(generaPayload());
        return firma.verify(ottieniFirma());
    }

    public byte[] ottieniFirma() {
        return firma;
    }

    public BigDecimal ottieniPrezzo() {
        return descrizione.ottieniPrezzo();
    }

    public long ottieniId() {
        return id;
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
}
