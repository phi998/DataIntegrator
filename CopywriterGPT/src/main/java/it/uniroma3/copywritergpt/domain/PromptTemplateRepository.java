package it.uniroma3.copywritergpt.domain;

import it.uniroma3.copywritergpt.domain.entity.PromptTemplate;
import org.springframework.data.repository.CrudRepository;

public interface PromptTemplateRepository extends CrudRepository<PromptTemplate, Long> {

}
