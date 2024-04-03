package it.uniroma3.copywritergpt.rest;

import it.uniroma3.copywritergpt.domain.ArticleService;
import it.uniroma3.di.common.api.dto.copywritergpt.GenerateArticleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class ArticleRestController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/articles/new")
    public GenerateArticleResponse generateArticle(
            @RequestParam String collectionName,
            @RequestParam Long templateId,
            @RequestParam String articleType,
            @RequestParam List<String> documentIds) {
        log.info("generateArticle(): templateId={}, articleType={}, documentIds={}, ",
                templateId, articleType, documentIds);

        return articleService.generateArticle(collectionName, documentIds, articleType, templateId);
    }


}
