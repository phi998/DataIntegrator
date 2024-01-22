package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class TablePreview {

    private Map<Integer, ColumnPreview> table; // column index 2 column preview

    public TablePreview() {
        this.table = new HashMap<>();
    }

    public void addTables(Map<String, Collection<String>> columnName2Cells) {
        int i = 0;

        for(Map.Entry<String, Collection<String>> colName2Cells : columnName2Cells.entrySet()) {
            ColumnPreview columnPreview = new ColumnPreview();
            columnPreview.setColumnName(colName2Cells.getKey());
            columnPreview.setCells(new ArrayList<>(colName2Cells.getValue()));
            this.table.put(i, columnPreview);
            i++;
        }
    }

}
