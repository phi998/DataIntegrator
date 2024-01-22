package it.uniroma3.copywritergpt.controller.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
public class TablePreviewForm {

    private Map<Integer, String> colIndex2Name;
    private Map<Integer, List<String>> colIndex2Preview;

    public TablePreviewForm() {
        this.colIndex2Preview = new TreeMap<>();
        this.colIndex2Name = new TreeMap<>();
    }

    public void addColumn(int index, String name, List<String> preview) {
        this.colIndex2Name.put(index, name);
        this.colIndex2Preview.put(index, preview);
    }

}
