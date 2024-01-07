package it.uniroma3.di.common.api.dto.copywritergpt;

import lombok.Data;

import java.util.Map;

@Data
public class GetDocumentsRequest {

    private Map<String,String> query;

}
