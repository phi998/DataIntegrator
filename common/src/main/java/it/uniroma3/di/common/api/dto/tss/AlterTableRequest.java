package it.uniroma3.di.common.api.dto.tss;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AlterTableRequest {

    private String collectionName;

    private Map<String,String> colName2type;

    public AlterTableRequest() {
        this.colName2type = new HashMap<>();
    }

}
