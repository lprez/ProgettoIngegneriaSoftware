package com.company.busbooking.server;

import com.company.busbooking.dominio.*;
import com.company.busbooking.interfacce.ServizioCliente;
import io.javalin.Javalin;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Date;

public class Server {
    private static final String fileChiave = "certificati/certificatochiave", password = "test123";
    private static final ServizioPagamento servizioPagamento = new ServizioPagamento("ServizioPagamento1") {
        @Override
        protected boolean pagaTentativo(Pagamento pagamento, int codice) {
            return true;
        }
    };

    public static void main(String[] args) {
        KeyPair chiavi = null;
        try {
            chiavi = caricaChiavi();
        } catch (Exception e) {
            System.err.println("Impossibile caricare la chiave/certificato.");
            e.printStackTrace();
            System.exit(1);
        }

        Javalin server = Javalin.create().start(8080);

        BusBooking busBooking = new BusBooking(chiavi, servizioPagamento);
        caricaDatiProva(busBooking);
        ServizioCliente servizioCliente = new GestoreCliente(busBooking);
        ServizioClienteProxyServer servizioClienteProxy = new ServizioClienteProxyServer(servizioCliente);
        servizioClienteProxy.inizializza(server);
    }

    private static KeyPair caricaChiavi()
            throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableEntryException, NoSuchProviderException {
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");

        try (FileInputStream fis2 = new FileInputStream(fileChiave)) {
            keyStore.load(fis2, password.toCharArray());
        }

        KeyStore.ProtectionParameter param = new KeyStore.PasswordProtection(password.toCharArray());
        KeyStore.TrustedCertificateEntry certificateEntry = (KeyStore.TrustedCertificateEntry) keyStore.getEntry("certificato", null);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("chiave", param);
        PublicKey publicKey = certificateEntry.getTrustedCertificate().getPublicKey();

        return new KeyPair(publicKey, privateKeyEntry.getPrivateKey());
    }

    private static void caricaDatiProva(BusBooking busBooking) {
        CatalogoBiglietti catalogo1 = new CatalogoBiglietti("Catania");
        CatalogoBiglietti catalogo2 = new CatalogoBiglietti("Palermo");

        DescrizioneBiglietto descrizione1 = new DescrizioneBiglietto(
                0, BigDecimal.valueOf(1.5), DescrizioneBiglietto.TipoBiglietto.ANDATA_RITORNO
        );
        DescrizioneBiglietto descrizione2 = new DescrizioneBiglietto(
                1, BigDecimal.valueOf(1), DescrizioneBiglietto.TipoBiglietto.CORSA_SINGOLA
        );
        DescrizioneBiglietto descrizione3 = new DescrizioneBiglietto(
                2, BigDecimal.valueOf(0.5), DescrizioneBiglietto.TipoBiglietto.TEMPO
        );

        catalogo1.aggiungiBiglietto(descrizione1);
        catalogo1.aggiungiBiglietto(descrizione2);

        catalogo2.aggiungiBiglietto(descrizione1);
        catalogo2.aggiungiBiglietto(descrizione2);
        catalogo2.aggiungiBiglietto(descrizione3);

        CartaDiCredito carta1 = new CartaDiCredito(0, "1234-5678-9123-4567", "Mario Rossi", new Date());
        CartaDiCredito carta2 = new CartaDiCredito(1, "9999-8888-7777-6666", "Luigi Rossi", new Date());

        Cliente cliente1 = new Cliente(0, "Luigi Rossi");
        cliente1.aggiungiCarta(carta2);

        busBooking.aggiungiCatalogo(catalogo1);
        busBooking.aggiungiCatalogo(catalogo2);

        busBooking.aggiungiCliente(cliente1);
    }
}
