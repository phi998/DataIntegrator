package it.uniroma3.di.common.api.dto.copywritergpt;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetPromptTemplateCategoryResponse {

    private Long id;

    private String name;

    private List<GetPromptTemplateResponse> templates;

    public GetPromptTemplateCategoryResponse() {
        this.templates = new ArrayList<>();
    }

    public void addTemplate(GetPromptTemplateResponse getPromptTemplateResponse) {
        this.templates.add(getPromptTemplateResponse);
    }

}
