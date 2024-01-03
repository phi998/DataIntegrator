package it.uniroma3.copywritergpt.domain;

import it.uniroma3.copywritergpt.domain.entity.PromptTemplate;
import it.uniroma3.copywritergpt.rest.dto.PromptTemplateCreatedResponse;
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

    public PromptTemplateCreatedResponse createPromptTemplate(Long categoryId, String templateName, String templateContent) {
        log.info("createPromptTemplate(): categoryId={}, templateName={}, templateContent={}", categoryId, templateName, templateContent);

        PromptTemplate promptTemplate = new PromptTemplate();
        promptTemplate.setName(templateName);
        promptTemplate.setContent(templateContent);
        promptTemplate.setCategory(promptCategoryService.getPromptCategoryById(categoryId));

        promptTemplate = promptTemplateRepository.save(promptTemplate);

        return Converter.toPromptTemplateCreatedResponse(promptTemplate);
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

    }

}
