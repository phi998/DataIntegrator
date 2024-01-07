package it.uniroma3.di.common.api.dto.tss;

import lombok.Data;

import java.util.Map;

@Data
public class QueryRequest {

    private Map<String,String> queryParams;

}
