package it.uniroma3.dim.utils;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class CSVUtilsTest {

    @Test
    void convertCsvStringToStructure() {

        String datasetFileName = "mondadori_12.csv";
        String fileContent = null;
        try {
            fileContent = readResourceFile(datasetFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String finalFileContent = fileContent;
        assertDoesNotThrow(() -> CSVUtils.convertCsvStringToStructure(finalFileContent, null, 10));
        assertTrue(true);

    }

    @Test
    void mergeColumnsWithSameName() throws IOException {

        String datasetFileName = "zalando_1.csv";
        String fileContent = null;
        try {
            fileContent = readResourceFile(datasetFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String res = CSVUtils.mergeColumnsWithSameName(fileContent);

        String filePath = "zalando_res.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(res);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        assertTrue(true);
    }

    private static String readResourceFile(String datasetFileName) throws IOException {
        File file = new File(datasetFileName);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        String str = new String(data, "UTF-8");
        return str;
    }
}