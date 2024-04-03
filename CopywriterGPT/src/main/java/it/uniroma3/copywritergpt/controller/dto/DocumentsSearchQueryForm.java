package it.uniroma3.copywritergpt.controller.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DocumentsSearchQueryForm {

    private Long templateId;

    private String collectionName;

    private String queryString;

    private Map<String,String> advancedQueryString;

    public DocumentsSearchQueryForm() {
        this.advancedQueryString = new HashMap<>();
    }

    public void addQueryStringItem(String item, String value) {
        this.advancedQueryString.put(item, value);
    }

}
