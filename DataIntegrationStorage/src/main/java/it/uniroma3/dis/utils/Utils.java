package it.uniroma3.dis.utils;

public class Utils {

    public static String getFileExtension(String fileName) {
        String fileExtension = "";

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            fileExtension = "." + fileName.substring(dotIndex + 1);
        }

        return fileExtension;
    }
}
