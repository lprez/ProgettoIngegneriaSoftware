package com.company.busbooking.appaddetto;

import com.company.busbooking.interfacce.ServizioAddetto;
import com.company.busbooking.util.Menu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private static final String fileChiave = "certificati/solocertificato", password = "test123";
    private static final String indirizzo = "http://localhost:8080";
    private static final long idAddetto = 0;

    private static final List<Comando> comandi = new ArrayList<Comando>(
            List.of(
                    new ComandoConvalidaBiglietto(),
                    new ComandoAggiornaCorsa(),
                    new ComandoEsci()
            )
    );

    public static void main(String[] args) throws IOException {
        PublicKey chiave = null;
        try {
            chiave = caricaChiave();
        } catch (Exception e) {
            System.err.println("Impossibile caricare la chiave/certificato.");
            e.printStackTrace();
            System.exit(1);
        }

        ServizioAddetto servizioFallback = new ServizioAddettoProxyOffline(chiave);
        ServizioAddetto servizio = new ServizioAddettoProxyClient(indirizzo, servizioFallback);

        Menu<Comando> menuComandi = new Menu<Comando>(
                comandi.stream().map(comando ->
                        new Menu.ElementoMenu<Comando>(comando.descrizione(), comando)
                ).toList()
        );

        Comando comando;
        while ((comando = menuComandi.mostra()) != null) {
            if (!comando.esegui(idAddetto, servizio)) {
                break;
            }
        }
    }

    private static PublicKey caricaChiave()
            throws KeyStoreException, NoSuchProviderException, IOException, UnrecoverableEntryException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");

        try (FileInputStream fis2 = new FileInputStream(fileChiave)) {
            keyStore.load(fis2, password.toCharArray());
        }

        KeyStore.ProtectionParameter param = new KeyStore.PasswordProtection(password.toCharArray());
        KeyStore.TrustedCertificateEntry certificateEntry = (KeyStore.TrustedCertificateEntry) keyStore.getEntry("certificato", null);
        PublicKey publicKey = certificateEntry.getTrustedCertificate().getPublicKey();

        return publicKey;
    }
}