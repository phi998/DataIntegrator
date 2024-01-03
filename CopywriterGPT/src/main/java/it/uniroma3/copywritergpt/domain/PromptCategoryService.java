package it.uniroma3.copywritergpt.domain;

import it.uniroma3.copywritergpt.domain.entity.PromptCategory;
import it.uniroma3.copywritergpt.rest.dto.PromptCategoryCreatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromptCategoryService {

    @Autowired
    private PromptCategoryRepository promptCategoryRepository;

    public PromptCategory getPromptCategoryByName(String name) {
        return promptCategoryRepository.findAllByName(name).get(0);
    }

    public PromptCategory getPromptCategoryById(Long categoryId) {
        return promptCategoryRepository.findById(categoryId).get();
    }

    public PromptCategoryCreatedResponse createNewPromptCategory(String categoryName) {

        PromptCategory category = new PromptCategory();
        category.setName(categoryName);
        category = promptCategoryRepository.save(category);

        return Converter.toPromptCategoryCreatedResponse(category);
    }

    private class Converter {

        static PromptCategoryCreatedResponse toPromptCategoryCreatedResponse(PromptCategory pc) {
            PromptCategoryCreatedResponse pccr = new PromptCategoryCreatedResponse();
            pccr.setId(pc.getId());
            pccr.setCategoryName(pc.getName());
            return pccr;
        }

    }

}
