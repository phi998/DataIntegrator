package it.uniroma3.tss.utils;

import it.uniroma3.tss.domain.vo.TableRow;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CSVUtilsTest {

    private String testString1 = "a,b,c\nd,e,f";
    private String testString2 = "a,b,a\nd,e,f";

    @Test
    void toTableRowsSimple() {
        Map<Integer, TableRow> rows = CSVUtils.toTableRows(testString1, List.of("a","b","c"));
        assertEquals("d", rows.get(0).getFieldName2Content().get("a"));
        assertEquals("e", rows.get(0).getFieldName2Content().get("b"));
        assertEquals("f", rows.get(0).getFieldName2Content().get("c"));
    }

    @Test
    void toTableRowsCase2() {
        Map<Integer, TableRow> rows = CSVUtils.toTableRows(testString2, List.of("a","b","a"));
        assertEquals("d", rows.get(0).getFieldName2Content().get("a"));
        assertEquals("e", rows.get(0).getFieldName2Content().get("b"));
    }

    /*@Test
    /*@Test
    void toTableRowsBig() {
        String csvString;
        try {
            csvString = this.readFile("restaurantguru_1.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<Integer, TableRow> rows = CSVUtils.toTableRows(csvString,CSVUtils.getTableHeaders(csvString));
        assertTrue(true);
    }*/


    @Test
    void mergeColumnsWithSameName() {
        String plainCsv = "A,B,A\na,b,a";
        String result = CSVUtils.mergeColumnsWithSameName(plainCsv);
        assertEquals("A,B\r\n\"a\na\",b\r\n", result);
    }

    private String readFile(String filename) throws IOException {
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        return new String(data, StandardCharsets.UTF_8);
    }

}