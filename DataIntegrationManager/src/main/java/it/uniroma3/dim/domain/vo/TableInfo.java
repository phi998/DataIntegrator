package it.uniroma3.dim.domain.vo;

import lombok.Data;

import java.util.Collection;

@Data
public class TableInfo {

    private String url;

    private Collection<String> columnNames;

}
