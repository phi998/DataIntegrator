package it.uniroma3.dim.rest.dto;

import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class TablesPreviewResponse {

    private Map<String, TablePreview> tables;

    public TablesPreviewResponse() {
        this.tables = new HashMap<>();
    }

    public void addTable(String tableName, Map<String, Collection<String>> columnName2Cells) {
        TablePreview tp = new TablePreview();
        tp.setTable(columnName2Cells);
        this.tables.put(tableName, tp);
    }

}
