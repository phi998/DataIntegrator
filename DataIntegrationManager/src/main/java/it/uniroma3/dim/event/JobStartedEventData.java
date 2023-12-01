package it.uniroma3.dim.event;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class JobStartedEventData {

    private Collection<String> ontology;

    private Collection<TableData> tables;

    public JobStartedEventData() {
        ontology = new ArrayList<>();
        tables = new ArrayList<>();
    }

    public void addTableData(String tableName, String tableContentResourceUrl) {
        TableData td = new TableData();
        td.setTableName(tableName);
        td.setTableContentResourceUrl(tableContentResourceUrl);
        tables.add(td);
    }

    @Data
    class TableData {

        private String tableName;

        private String tableContentResourceUrl;

    }

}
