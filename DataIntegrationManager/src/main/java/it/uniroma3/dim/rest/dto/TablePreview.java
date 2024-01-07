package it.uniroma3.dim.rest.dto;

import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class TablePreview {

    private Map<String, Collection<String>> table;

    public TablePreview() {
        this.table = new HashMap<>();
    }

}
