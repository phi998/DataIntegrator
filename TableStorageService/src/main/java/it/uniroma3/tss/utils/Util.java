package it.uniroma3.tss.utils;

public class Util {

    public static String convertToSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder snakeCase = new StringBuilder();
        boolean isFirstChar = true;

        for (char currentChar : input.toCharArray()) {
            if (Character.isLetterOrDigit(currentChar)) {
                if (Character.isUpperCase(currentChar) && !isFirstChar) {
                    snakeCase.append('_');
                }
                snakeCase.append(Character.toLowerCase(currentChar));
                isFirstChar = false;
            } else if (currentChar == ' ') {
                snakeCase.append('_');
                isFirstChar = true;
            }
        }

        return snakeCase.toString();
    }

}
