package it.uniroma3.tss.domain.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TableRow {

    private Map<String, String> fieldName2Content;

    public TableRow() {
        this.fieldName2Content = new HashMap<>();
    }

    public void addCell(String fieldName, String content) {
        this.fieldName2Content.put(fieldName,content);
    }

}
