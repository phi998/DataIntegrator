package it.uniroma3.di.common.api.dto.copywritergpt;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetCategoriesCollectionResponse {

    private List<GetPromptTemplateCategoryResponse> categories;

    public GetCategoriesCollectionResponse() {
        this.categories = new ArrayList<>();
    }

    public void addCategory(GetPromptTemplateCategoryResponse getPromptTemplateCategoryResponse) {
        this.categories.add(getPromptTemplateCategoryResponse);
    }

}
