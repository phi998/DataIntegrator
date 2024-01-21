package it.uniroma3.dim.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

public class CSVUtils {

    public static String sample(String csvString, boolean hasHeader, int n) {
        StringBuilder sampledLines = new StringBuilder();

        try (StringReader stringReader = new StringReader(csvString);
             CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(stringReader)) {

            int count = 0;
            for (CSVRecord record : csvParser) {
                if (count >= n) {
                    break;
                }
                sampledLines.append(record.toString()).append("\n");
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sampledLines.toString();
    }

    public static Map<String, Collection<String>> convertCsvStringToStructure(String csvTable) {
        Map<String, Collection<String>> csvStructure = new HashMap<>();

        try (StringReader stringReader = new StringReader(csvTable);
             CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(stringReader)) {

            String[] headers = csvParser.getHeaderMap().keySet().toArray(new String[0]);

            for (String header : headers) {
                csvStructure.put(header, new ArrayList<>());
            }

            for (CSVRecord record : csvParser) {
                for (String header : headers) {
                    csvStructure.get(header).add(record.get(header));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return csvStructure;
    }

    public static String mergeColumnsWithSameName(String csvString) throws IOException {
        StringReader stringReader = new StringReader(csvString);
        CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(stringReader);
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);

        Map<String, Integer> columnIndexes = new HashMap<>();

        String[] header = csvParser.getHeaderMap().keySet().toArray(new String[0]);
        for (int i = 0; i < header.length; i++) {
            String columnName = header[i];
            if (!columnIndexes.containsKey(columnName)) {
                columnIndexes.put(columnName, i);
            } else {
                header[i] = columnName + "_" + columnIndexes.get(columnName);
                columnIndexes.put(columnName, columnIndexes.get(columnName) + 1);
            }
        }
        csvPrinter.printRecord(header);

        for (CSVRecord record : csvParser) {
            csvPrinter.printRecord(record);
        }

        csvPrinter.close();
        csvParser.close();

        return stringWriter.toString();
    }

}
