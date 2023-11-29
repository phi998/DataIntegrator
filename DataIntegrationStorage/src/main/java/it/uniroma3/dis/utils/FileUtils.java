package it.uniroma3.dis.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {

    public static void saveStoredFile(String assignedFileName, byte[] fileContent) {
        String filePath = Constants.DATA_DIRECTORY + assignedFileName;

        try {
            Path path = Paths.get(filePath);
            Files.write(path, fileContent);

            log.info("File saved successfully: {}", filePath);
        } catch (Exception e) {
            log.error("Error saving file: {}", e.getMessage());
        }
    }

    public static byte[] getFileContent(String assignedFileName) {
        String filePath = Constants.DATA_DIRECTORY + assignedFileName;

        Path path = Paths.get(filePath);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("Error while reading file: {}", e.getMessage());
        }

        return null;
    }

    public static void deleteStoredFile(String assignedFileName) {
        String filePath = Constants.DATA_DIRECTORY + assignedFileName;

        try {
            Path path = Paths.get(filePath);
            Files.delete(path);
            log.info("File deleted successfully: {}", filePath);
        } catch (IOException e) {
            log.error("Error deleting file {}", e.getMessage());
        }
    }
}
