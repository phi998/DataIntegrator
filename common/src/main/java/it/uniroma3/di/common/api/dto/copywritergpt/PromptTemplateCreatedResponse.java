package it.uniroma3.di.common.api.dto.copywritergpt;

import lombok.Data;

@Data
public class PromptTemplateCreatedResponse {

    private Long id;

    private String name;

    private String category;

    private Long categoryId;

}
