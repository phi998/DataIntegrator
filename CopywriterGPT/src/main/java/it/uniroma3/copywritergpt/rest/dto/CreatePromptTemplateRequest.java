package it.uniroma3.copywritergpt.rest.dto;

import lombok.Data;

@Data
public class CreatePromptTemplateRequest {

    private String name;

    private String content;

}
