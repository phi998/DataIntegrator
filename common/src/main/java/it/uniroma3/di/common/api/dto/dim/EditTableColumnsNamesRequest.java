package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class EditTableColumnsNamesRequest {

    private Map<String, Collection<String>> table2colnames;

    public EditTableColumnsNamesRequest() {
        this.table2colnames = new HashMap<>();
    }

    public void addTable(String tableName, Collection<String> colNames) {
        this.table2colnames.put(tableName, colNames);
    }
}
