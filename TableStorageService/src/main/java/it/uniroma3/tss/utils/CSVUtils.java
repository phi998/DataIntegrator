package it.uniroma3.tss.utils;

import it.uniroma3.tss.domain.vo.TableRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class CSVUtils {

    public static Map<Integer, TableRow> toTableRows(String csvString, Collection<String> headers) {
        log.info("toTableRows(): csvString={}, headers={}", csvString, headers);

        Map<Integer, TableRow> rows = new TreeMap<>();

        try {
            StringReader stringReader = new StringReader(csvString);

            CSVParser csvParser = CSVParser.parse(stringReader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            int currentRow = 0;
            for (CSVRecord record : csvParser) {
                rows.put(currentRow, new TableRow());
                for(String header: headers) {
                    if(record.isSet(header))
                        rows.get(currentRow).addCell(header, record.get(header));
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

    public static List<String> getTableHeaders(String csvString) {
        StringReader stringReader = new StringReader(csvString);

        String[] headerNames = null;

        try {
            CSVParser csvParser = new CSVParser(stringReader, CSVFormat.DEFAULT.withHeader());

            headerNames = csvParser.getHeaderMap().keySet().toArray(new String[0]);

            csvParser.close();
            stringReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return List.of(headerNames);
    }

}
