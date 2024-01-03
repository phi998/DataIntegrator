package it.uniroma3.dim.utils;

import java.util.Base64;

public class Utils {

    public static String convertBinaryToString(byte[] binaryData) {
        return new String(binaryData);
    }

    public static String convertToBase64String(String originalString) {
        byte[] bytes = originalString.getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        return new String(encodedBytes);
    }

}
