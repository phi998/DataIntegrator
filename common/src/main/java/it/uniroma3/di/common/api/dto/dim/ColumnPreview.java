package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ColumnPreview {

    private String columnName;

    private List<String> cells;

    public ColumnPreview() {
        this.cells = new ArrayList<>();
    }

}
