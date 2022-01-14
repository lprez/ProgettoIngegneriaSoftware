package com.company.busbooking.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input {
    public static int leggiIntero() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            throw new IOException(e);
        }
    }

    public static byte[] leggiArrayByte() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (!line.startsWith("\"")) line = "\"" + line + "\"";
        try {
            return new ObjectMapper().readValue(line, byte[].class);
        } catch (MismatchedInputException e) {
            return null;
        }
    }
}
