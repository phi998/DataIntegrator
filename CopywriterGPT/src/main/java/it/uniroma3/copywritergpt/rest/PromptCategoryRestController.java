package it.uniroma3.copywritergpt.rest;

import it.uniroma3.copywritergpt.domain.PromptCategoryService;
import it.uniroma3.di.common.api.dto.copywritergpt.CreatePromptCategoryRequest;
import it.uniroma3.di.common.api.dto.copywritergpt.GetCategoriesCollectionResponse;
import it.uniroma3.di.common.api.dto.copywritergpt.PromptCategoryCreatedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PromptCategoryRestController {

    @Autowired
    private PromptCategoryService promptCategoryService;


    @PostMapping("/category")
    public PromptCategoryCreatedResponse createPromptCategory(@RequestBody CreatePromptCategoryRequest createPromptCategoryRequest) {
        log.info("createPromptCategory(): createPromptCategoryRequest={}", createPromptCategoryRequest);
        return promptCategoryService.createNewPromptCategory(createPromptCategoryRequest.getName());
    }

    @GetMapping("/category")
    public GetCategoriesCollectionResponse getCategories(@RequestParam("name") String categoryName) {
        log.info("getCategories(): name={}", categoryName);

        GetCategoriesCollectionResponse categories = promptCategoryService.getAllCategories(categoryName);

        log.info("getCategories(): categories={}", categories);

        return categories;
    }

}
