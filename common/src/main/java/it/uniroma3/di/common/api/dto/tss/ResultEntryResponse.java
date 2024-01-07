package it.uniroma3.di.common.api.dto.tss;

import lombok.Data;

import java.util.Map;

@Data
public class ResultEntryResponse {

    private Map<String, String> columnName2Content;

}
