package it.uniroma3.dim.event;

import lombok.Data;

import java.util.List;

@Data
public class JobTableEventData {

    private String tableName;

    private String tableUrl;

    private List<String> columnNames;

}
