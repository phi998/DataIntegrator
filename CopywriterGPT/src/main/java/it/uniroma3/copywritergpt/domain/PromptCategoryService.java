package it.uniroma3.copywritergpt.domain;

import it.uniroma3.copywritergpt.domain.entity.PromptCategory;
import it.uniroma3.copywritergpt.domain.entity.PromptTemplate;
import it.uniroma3.di.common.api.dto.copywritergpt.GetCategoriesCollectionResponse;
import it.uniroma3.di.common.api.dto.copywritergpt.GetPromptTemplateCategoryResponse;
import it.uniroma3.di.common.api.dto.copywritergpt.GetPromptTemplateResponse;
import it.uniroma3.di.common.api.dto.copywritergpt.PromptCategoryCreatedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PromptCategoryService {

    @Autowired
    private PromptCategoryRepository promptCategoryRepository;

    public PromptCategory getPromptCategoryByName(String name) {

        List<PromptCategory> categories = promptCategoryRepository.findAllByName(name);
        if(categories.isEmpty())
            return null;

        return categories.get(0);
    }

    public PromptCategory getPromptCategoryById(Long categoryId) {
        Optional<PromptCategory> promptCategory = promptCategoryRepository.findById(categoryId);

        return promptCategory.orElse(null);
    }

    public PromptCategory createNewCategory(String categoryName) {
        PromptCategory category = new PromptCategory();
        category.setName(categoryName);
        return promptCategoryRepository.save(category);
    }

    public PromptCategoryCreatedResponse createNewPromptCategory(String categoryName) {

        PromptCategory category = new PromptCategory();
        category.setName(categoryName);
        category = promptCategoryRepository.save(category);

        return Converter.toPromptCategoryCreatedResponse(category);
    }

    public GetCategoriesCollectionResponse getAllCategories(String name) {
        log.info("getAllCategories()");
        GetCategoriesCollectionResponse getCategoriesCollectionResponse = new GetCategoriesCollectionResponse();

        if(name == null || name.equals("")) {
            this.promptCategoryRepository.findAll().forEach(
                    pc -> getCategoriesCollectionResponse.addCategory(Converter.toGetPromptTemplateCategoryResponse(pc)));
        } else {
            this.promptCategoryRepository.findAllByName(name).forEach(
                    pc -> getCategoriesCollectionResponse.addCategory(Converter.toGetPromptTemplateCategoryResponse(pc)));
        }

        return getCategoriesCollectionResponse;
    }

    public GetPromptTemplateCategoryResponse getPromptTemplateCategoryByName(String name) {
        return Converter.toGetPromptTemplateCategoryResponse(this.getPromptCategoryByName(name));
    }



    private class Converter {

        static PromptCategoryCreatedResponse toPromptCategoryCreatedResponse(PromptCategory pc) {
            PromptCategoryCreatedResponse pccr = new PromptCategoryCreatedResponse();
            pccr.setId(pc.getId());
            pccr.setCategoryName(pc.getName());
            return pccr;
        }

        static GetPromptTemplateCategoryResponse toGetPromptTemplateCategoryResponse(PromptCategory promptCategory) {
            GetPromptTemplateCategoryResponse getPromptTemplateCategoryResponse = new GetPromptTemplateCategoryResponse();
            getPromptTemplateCategoryResponse.setId(promptCategory.getId());
            getPromptTemplateCategoryResponse.setName(promptCategory.getName());

            promptCategory.getTemplates().forEach(t -> getPromptTemplateCategoryResponse.addTemplate(toGetPromptTemplateResponse(t)));

            return getPromptTemplateCategoryResponse;
        }

        static GetPromptTemplateResponse toGetPromptTemplateResponse(PromptTemplate promptTemplate) {
            GetPromptTemplateResponse getPromptTemplateResponse = new GetPromptTemplateResponse();
            getPromptTemplateResponse.setId(promptTemplate.getId());
            getPromptTemplateResponse.setContent(promptTemplate.getContent());
            getPromptTemplateResponse.setName(promptTemplate.getName());

            return getPromptTemplateResponse;
        }

    }

}
