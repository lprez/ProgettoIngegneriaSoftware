package com.company.busbooking.dominio.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class GeneratoreChiavi {
    public static KeyPair generaChiavi() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair chiave = keyPairGenerator.generateKeyPair();
        return chiave;
    }
}
