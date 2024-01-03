package it.uniroma3.copywritergpt.domain;

import it.uniroma3.copywritergpt.domain.entity.PromptCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface PromptCategoryRepository extends CrudRepository<PromptCategory, Long> {

    List<PromptCategory> findAllByName(String name);

}
