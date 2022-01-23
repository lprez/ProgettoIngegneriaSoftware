package com.company.busbooking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input {
    public static Integer leggiIntero() throws IOException {
        try {
            return Integer.parseInt(leggiStringa());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static byte[] leggiArrayByte() throws IOException {
        String linea = leggiStringa();
        if (!linea.startsWith("\"")) linea = "\"" + linea + "\"";
        try {
            return new ObjectMapper().readValue(linea, byte[].class);
        } catch (MismatchedInputException e) {
            return null;
        }
    }

    public static String leggiStringa() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static boolean leggisN() throws IOException {
        return leggiStringa().startsWith("s");
    }
}
