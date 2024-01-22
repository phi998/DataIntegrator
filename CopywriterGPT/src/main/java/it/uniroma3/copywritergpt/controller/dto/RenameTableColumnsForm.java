package it.uniroma3.copywritergpt.controller.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RenameTableColumnsForm {

    private Map<String, TablePreviewForm> tableName2Table;

    public RenameTableColumnsForm() {
        this.tableName2Table = new HashMap<>();
    }

    public void addTable(String tableName, TablePreviewForm tablePreviewForm) {
        this.tableName2Table.put(tableName, tablePreviewForm);
    }

}
