package it.uniroma3.copywritergpt.domain;

import it.uniroma3.copywritergpt.domain.entity.PromptTemplate;
import it.uniroma3.di.common.api.dto.copywritergpt.GetPromptTemplateResponse;
import it.uniroma3.di.common.api.dto.copywritergpt.PromptTemplateCreatedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PromptTemplateService {

    @Autowired
    private PromptTemplateRepository promptTemplateRepository;

    @Autowired
    private PromptCategoryService promptCategoryService;

    public PromptTemplate getPromptTemplateById(Long templateId) {
        return this.promptTemplateRepository.findById(templateId).get();
    }

    public PromptTemplateCreatedResponse createPromptTemplate(Long categoryId, String templateName, String templateContent) {
        log.info("createPromptTemplate(): categoryId={}, templateName={}, templateContent={}", categoryId, templateName, templateContent);

        PromptTemplate promptTemplate = new PromptTemplate();
        promptTemplate.setName(templateName);
        promptTemplate.setContent(templateContent);
        promptTemplate.setCategory(promptCategoryService.getPromptCategoryById(categoryId));

        promptTemplate = promptTemplateRepository.save(promptTemplate);

        return Converter.toPromptTemplateCreatedResponse(promptTemplate);
    }

    public GetPromptTemplateResponse getPromptTemplate(Long templateId) {
        log.info("getPromptTemplate(): templateId={}", templateId);

        return Converter.toGetPromptTemplateResponse(promptTemplateRepository.findById(templateId).get());
    }

    private class Converter {

        static PromptTemplateCreatedResponse toPromptTemplateCreatedResponse(PromptTemplate promptTemplate) {
            PromptTemplateCreatedResponse promptTemplateCreatedResponse = new PromptTemplateCreatedResponse();
            promptTemplateCreatedResponse.setId(promptTemplate.getId());
            promptTemplateCreatedResponse.setName(promptTemplate.getName());
            promptTemplateCreatedResponse.setCategoryId(promptTemplate.getCategory().getId());
            promptTemplateCreatedResponse.setCategory(promptTemplate.getCategory().getName());
            return promptTemplateCreatedResponse;
        }

        static GetPromptTemplateResponse toGetPromptTemplateResponse(PromptTemplate promptTemplate) {
            GetPromptTemplateResponse getPromptTemplateResponse = new GetPromptTemplateResponse();
            getPromptTemplateResponse.setId(promptTemplate.getId());
            getPromptTemplateResponse.setName(promptTemplate.getName());
            getPromptTemplateResponse.setContent(promptTemplate.getContent());

            return getPromptTemplateResponse;
        }

    }

}
