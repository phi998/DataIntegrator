package it.uniroma3.tss.utils;

import it.uniroma3.tss.domain.vo.TableRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

@Slf4j
public class CSVUtils {

    public static Map<Integer, TableRow> toTableRows(String csvString, List<String> headers) {
        headers = CSVUtils.getTableHeaders(csvString);
        log.info("toTableRows(): csvString={}, headers={}", csvString, headers);

        Map<Integer, TableRow> rows = new TreeMap<>();

        try {
            StringReader stringReader = new StringReader(csvString);

            CSVParser csvParser = CSVParser.parse(stringReader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            int currentRow = 0;
            int headersLength = headers.size();
            for (CSVRecord record : csvParser) {
                rows.put(currentRow, new TableRow());
                for(int i = 0; i < headersLength; i++) {
                    String cellContent = record.get(i);
                    String header = headers.get(i);
                    rows.get(currentRow).addCell(header, cellContent);
                }
                currentRow++;
            }
            csvParser.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("toTableRows(): rows={}", rows);

        return rows;
    }

    public static String mergeColumnsWithSameName(String inputCSV) {
        try {
            CSVParser parser = new CSVParser(new StringReader(inputCSV), CSVFormat.DEFAULT.withHeader());
            Map<String, Integer> headerMap = new HashMap<>();
            StringWriter writer = new StringWriter();
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

            int h = 0;
            int hh = 0;
            Map<Integer,String> index2HeaderName = new TreeMap<>();
            for (String header : parser.getHeaderNames()) {
                index2HeaderName.put(hh, header); hh++;

                if(!headerMap.containsKey(header)) {
                    headerMap.put(header, h);
                    h++;
                    printer.print(header);
                }
            }
            printer.println();

            for (CSVRecord record : parser) {
                Map<String, String> header2Content = new HashMap<>();
                for(int i = 0; i < record.size(); i++) {
                    String content = record.get(i);
                    String headerName = index2HeaderName.get(i);

                    header2Content.putIfAbsent(headerName, "");
                    String toPrint = String.join("\n", content, header2Content.get(headerName)).trim();
                    header2Content.put(headerName, toPrint);
                }

                for(Map.Entry<String, String> entry: header2Content.entrySet())
                    printer.print(entry.getValue());

                printer.println();
            }

            printer.close();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String deleteColumnsByName(String csvString, String... columnNamesToDelete) {
        // Parse CSV string
        try {
            CSVParser csvParser = CSVParser.parse(new StringReader(csvString), CSVFormat.DEFAULT.withHeader());
            List<String> headers = new ArrayList<>(csvParser.getHeaderNames());

            // Remove columns to delete from headers
            for (String columnName : columnNamesToDelete) {
                headers.remove(columnName);
            }

            // Create CSV printer
            StringWriter stringWriter = new StringWriter();
            CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])));

            // Iterate over records and print
            for (CSVRecord record : csvParser) {
                for (String header : headers) {
                    csvPrinter.print(record.get(header));
                }
                csvPrinter.println();
            }

            // Close resources
            csvParser.close();
            csvPrinter.close();

            return stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String considerFirstColByName(String csvString) {
        try {
            // Parse CSV string
            CSVParser parser = new CSVParser(new StringReader(csvString), CSVFormat.DEFAULT.withHeader());
            List<String> headerNames = parser.getHeaderNames();

            // Map to store the index of the first occurrence of each column name
            Map<String, Integer> firstColumnIndices = new HashMap<>();

            // Find the first occurrence of each column name
            for (int i = 0; i < headerNames.size(); i++) {
                String columnName = headerNames.get(i);
                if (!firstColumnIndices.containsKey(columnName)) {
                    firstColumnIndices.put(columnName, i);
                }
            }

            // Build the new CSV string with only the first occurrence of each column
            StringWriter writer = new StringWriter();
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

            // Write header
            List<String> newHeader = new ArrayList<>(firstColumnIndices.keySet());
            printer.printRecord(newHeader);

            // Write data
            for (CSVRecord record : parser) {
                List<String> newRow = new ArrayList<>();
                for (String columnName : newHeader) {
                    int columnIndex = firstColumnIndices.get(columnName);
                    newRow.add(record.get(columnIndex));
                }
                printer.printRecord(newRow);
            }

            // Close resources
            printer.close();
            parser.close();

            // Get the new CSV string
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static List<String> getTableHeaders(String csvString) {
        String[] columns = csvString.split("\n")[0].split(",");
        return Arrays.asList(columns);
    }

}
