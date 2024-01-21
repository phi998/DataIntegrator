package it.uniroma3.copywritergpt.utils;

import java.util.Base64;

public class Util {

    public static String convertToBase64String(String originalString) {
        byte[] bytes = originalString.getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        return new String(encodedBytes);
    }

}
