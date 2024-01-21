package it.uniroma3.copywritergpt.controller.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PreviewRenameColumnsForm {

    private Map<String, RenameTableColumnsForm> tableName2newColumnsNames;

    public PreviewRenameColumnsForm() {
        this.tableName2newColumnsNames = new HashMap<>();
    }

}
