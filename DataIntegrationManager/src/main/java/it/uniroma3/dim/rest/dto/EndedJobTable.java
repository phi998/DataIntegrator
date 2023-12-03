package it.uniroma3.dim.rest.dto;

import lombok.Data;

@Data
public class EndedJobTable {

    private String tableName;

    private byte[] tableContent;

}
