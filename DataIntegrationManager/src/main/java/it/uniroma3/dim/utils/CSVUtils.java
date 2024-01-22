package it.uniroma3.dim.utils;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import it.uniroma3.di.common.api.dto.dim.ColumnPreview;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

public class CSVUtils {

    public static String sample(String csvString, boolean hasHeader, int n) {
        try (CSVReader csvReader = new CSVReader(new StringReader(csvString))) {
            // Read the specified number of lines
            List<String[]> sampledLines = csvReader.readAll();

            // Limit the list to the specified number of lines
            if (sampledLines.size() > n) {
                sampledLines = sampledLines.subList(0, n);
            }

            // Convert the sampled lines to a string
            String result = sampledLines.stream()
                    .map(line -> String.join(",", Arrays.asList(line)))
                    .collect(Collectors.joining("\n"));

            return result;
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return null; // Return null in case of an error
    }

    public static Map<Integer, ColumnPreview> convertCsvStringToStructure(String csvTable, List<String> headers, int n) {
        if(n==0)
            n = Integer.MAX_VALUE;

        Map<Integer, ColumnPreview> csvStructure = new TreeMap<>();

        try (StringReader stringReader = new StringReader(csvTable);
             CSVReader csvReader = new CSVReaderBuilder(stringReader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(',').withQuoteChar('"')
                     .withEscapeChar('\\').build())
                     .build()) {

            String[] headersArray = csvReader.readNext();

            if (headersArray != null) {
                headers = Arrays.asList(headersArray);
            }

            int i = 0;
            for (String header : headers) {
                ColumnPreview cp = new ColumnPreview();
                cp.setColumnName(header);
                csvStructure.put(i, cp);
                i++;
            }

            List<String[]> records = csvReader.readAll();
            for(i = 0; i < n; i++) {
                String[] record = records.get(i);
                int nElements = headers.size();
                for (int j = 0; j < nElements; j++) {
                    csvStructure.get(j).getCells().add(record[j]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return csvStructure;
    }

    public static String mergeColumnsWithSameName(String inputCSV) throws IOException {
        try {
            // Read the CSV string
            CSVParser parser = new CSVParser(new StringReader(inputCSV), CSVFormat.DEFAULT.withHeader());
            Map<String, Integer> headerMap = new HashMap<>();
            StringWriter writer = new StringWriter();
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

            // Write the header
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

            // Merge columns with the same header name and write the merged values
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
            e.printStackTrace(); // Handle the exception according to your needs
            return null;
        }
    }

}
