package it.uniroma3.tss.domain.vo;

import lombok.Data;

import java.util.Map;

@Data
public class ResultEntry {

    private Map<String, String> columnName2Content;

}
