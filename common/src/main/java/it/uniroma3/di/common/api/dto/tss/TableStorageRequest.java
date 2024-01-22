package it.uniroma3.di.common.api.dto.tss;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class TableStorageRequest {

    private String tableName;

    private String category;

    private String content;

    private List<TableStorageField> fields;

    private boolean hasHeader;

    public TableStorageRequest() {
        this.fields = new ArrayList<>();
    }

    public void addField(TableStorageField field) {
        this.fields.add(field);
    }

}
