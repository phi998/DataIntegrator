package it.uniroma3.tss.rest.dto;

import lombok.Data;

import java.util.Map;

@Data
public class QueryRequest {

    private Map<String,String> queryParams;

}
