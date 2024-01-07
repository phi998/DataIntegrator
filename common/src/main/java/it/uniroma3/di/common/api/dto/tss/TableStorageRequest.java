package it.uniroma3.di.common.api.dto.tss;

import lombok.Data;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString(exclude = "content")
public class TableStorageRequest {

    private String tableName;

    private String category;

    private String content;

    private Collection<TableStorageField> fields;

    private boolean hasHeader;

    public void addField(TableStorageField field) {
        this.fields.add(field);
    }

}
