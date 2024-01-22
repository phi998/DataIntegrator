package it.uniroma3.di.common.api.dto.dim;

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

    public void addTable(String tableName, Map<Integer, ColumnPreview> colIndex2Column) {
        TablePreview tp = new TablePreview();
        tp.setTable(colIndex2Column);
        this.tables.put(tableName, tp);
    }

}
