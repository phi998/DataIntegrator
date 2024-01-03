package it.uniroma3.copywritergpt.rest;

import it.uniroma3.copywritergpt.domain.PromptTemplateService;
import it.uniroma3.copywritergpt.rest.dto.CreatePromptTemplateRequest;
import it.uniroma3.copywritergpt.rest.dto.PromptTemplateCreatedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PromptTemplateController {

    @Autowired
    private PromptTemplateService promptTemplateService;

    @PostMapping("/category/{categoryId}/template")
    public PromptTemplateCreatedResponse createPromptTemplate(@PathVariable("categoryId") Long categoryId, @RequestBody CreatePromptTemplateRequest createPromptTemplateRequest) {
        log.info("createPromptTemplate(): categoryId={}, createPromptTemplateRequest={}", categoryId, createPromptTemplateRequest);

        return promptTemplateService.createPromptTemplate(categoryId, createPromptTemplateRequest.getName(), createPromptTemplateRequest.getContent());
    }

}
