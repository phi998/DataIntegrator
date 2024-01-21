package it.uniroma3.copywritergpt.domain;

import it.uniroma3.copywritergpt.domain.entity.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

}
