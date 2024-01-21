package it.uniroma3.copywritergpt.controller;

import it.uniroma3.copywritergpt.domain.ArticleService;
import it.uniroma3.di.common.api.dto.copywritergpt.GenerateArticleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
public class ArticlesController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/articles/new")
    public String generateArticle(
            @RequestParam Long templateId,
            @RequestParam String articleType,
            @RequestParam List<String> documentIds,
            Model model) {
        log.info("generateArticle(): templateId={}, articleType={}, documentIds={}",
                templateId,articleType,documentIds);

        GenerateArticleResponse generateArticleResponse = this.articleService.generateArticle(
                documentIds,articleType,templateId);

        model.addAttribute("articleContent", generateArticleResponse.getArticleContent());

        return "article";
    }

}
