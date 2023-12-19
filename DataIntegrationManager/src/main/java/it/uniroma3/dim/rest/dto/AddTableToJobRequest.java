package it.uniroma3.dim.rest.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class AddTableToJobRequest {

    private String tableName;

    private String tableContent; // table content encoded in base64 format

    private Collection<Integer> columnsToIgnoreIndexes;

}
