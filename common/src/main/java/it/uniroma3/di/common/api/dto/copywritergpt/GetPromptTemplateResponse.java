package it.uniroma3.di.common.api.dto.copywritergpt;

import lombok.Data;

@Data
public class GetPromptTemplateResponse {

    private Long id;

    private String name;

    private String content;

}
