package it.uniroma3.dim.event;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class JobStartedEventData {

    private String ontologyName;

    private Map<String,OntologyItem> ontologyItems;

    private Collection<Integer> columnsToDrop;

    private Collection<TableData> tables;

    public JobStartedEventData() {
        ontologyItems = new HashMap<>();
        tables = new ArrayList<>();
    }

    public void addTableData(String tableName, String tableContentResourceUrl) {
        TableData td = new TableData();
        td.setTableName(tableName);
        td.setTableContentResourceUrl(tableContentResourceUrl);
        tables.add(td);
    }

    public void addOntologyItem(String label, int importance, String type, String notes) {
        OntologyItem oi = new OntologyItem();
        oi.setType(type);
        oi.setImportance(importance);
        oi.setNotes(notes);

        this.ontologyItems.put(label, oi);
    }

    @Data
    static class TableData {

        private String tableName;

        private String tableContentResourceUrl;

    }

}
