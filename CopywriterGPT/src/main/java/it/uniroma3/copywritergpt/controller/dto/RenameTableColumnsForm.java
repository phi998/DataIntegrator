package it.uniroma3.copywritergpt.controller.dto;

import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

@Data
public class RenameTableColumnsForm {

    private Map<Integer, String> colIndex2newName;

    public RenameTableColumnsForm() {
        this.colIndex2newName = new TreeMap<>();
    }

}
