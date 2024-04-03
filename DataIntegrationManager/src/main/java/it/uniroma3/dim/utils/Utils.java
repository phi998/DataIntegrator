package it.uniroma3.dim.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Utils {

    public static String convertBinaryToString(byte[] binaryData) {
        return new String(binaryData, StandardCharsets.UTF_8);
    }

    public static String convertToBase64String(String originalString) {
        byte[] bytes = originalString.getBytes(StandardCharsets.UTF_8);
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    public static String convertToSnakeCase(String input) {
        input = input.replaceAll("/", " ");

        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder snakeCase = new StringBuilder();
        boolean lastCharWasSpace = false;

        for (char currentChar : input.toCharArray()) {
            if (Character.isWhitespace(currentChar)) {
                if (!lastCharWasSpace) {
                    snakeCase.append('_');
                    lastCharWasSpace = true;
                }
            } else if (Character.isUpperCase(currentChar) || Character.isDigit(currentChar)) {
                snakeCase.append('_');
                snakeCase.append(Character.toLowerCase(currentChar));
                lastCharWasSpace = false;
            } else {
                snakeCase.append(currentChar);
                lastCharWasSpace = false;
            }
        }

        return snakeCase.toString();
    }

}
