package it.uniroma3.copywritergpt.rest;

import it.uniroma3.copywritergpt.domain.PromptCategoryService;
import it.uniroma3.copywritergpt.rest.dto.CreatePromptCategoryRequest;
import it.uniroma3.copywritergpt.rest.dto.PromptCategoryCreatedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
