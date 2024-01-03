package it.uniroma3.tss.rest.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ResultEntryResponse {

    private Map<String, String> columnName2Content;

}
