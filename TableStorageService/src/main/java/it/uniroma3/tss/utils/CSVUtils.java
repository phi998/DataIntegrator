package it.uniroma3.tss.utils;

import it.uniroma3.tss.domain.vo.TableRow;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class CSVUtils {

    public static Map<Integer, TableRow> toTableRows(String csvString, Collection<String> headers) {
        Map<Integer, TableRow> rows = new TreeMap<>();

        try {
            StringReader stringReader = new StringReader(csvString);

            CSVParser csvParser = CSVParser.parse(stringReader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            int currentRow = 0;
            for (CSVRecord record : csvParser) {
                rows.put(currentRow, new TableRow());
                for(String header: headers) {
                    if(record.isSet(header)) {
                        rows.get(currentRow).addCell(header, record.get(header));
                    }
                }
                currentRow++;
            }
            csvParser.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rows;
    }

}
