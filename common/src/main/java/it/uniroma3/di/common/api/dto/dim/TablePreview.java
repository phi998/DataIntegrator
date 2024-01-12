package it.uniroma3.di.common.api.dto.dim;

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
