package it.uniroma3.di.common.api.dto.copywritergpt;

import lombok.Data;

@Data
public class CreatePromptTemplateRequest {

    private String name;

    private String content;

}
