package it.uniroma3.copywritergpt.controller.dto;

import lombok.Data;

@Data
public class DocumentsSearchQueryForm {

    private Long templateId;

    private String queryString;

}
