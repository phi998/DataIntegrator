package it.uniroma3.copywritergpt.controller;

import it.uniroma3.copywritergpt.domain.ArticleService;
import it.uniroma3.copywritergpt.enums.ArticleType;
import it.uniroma3.di.common.api.dto.copywritergpt.DocumentResultEntry;
import it.uniroma3.di.common.api.dto.copywritergpt.DocumentsSelectionForm;
import it.uniroma3.di.common.api.dto.copywritergpt.GenerateArticleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class ArticlesController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/articles/new")
    public String generateArticle(
            @ModelAttribute("documentsForm") DocumentsSelectionForm documentsSelectionForm,
            Model model) {
        log.info("generateArticle(): documentsSelectionForm={}", documentsSelectionForm);

        List<DocumentResultEntry> documents = documentsSelectionForm.getDocuments().stream().filter(DocumentResultEntry::isSelected).toList();
        documentsSelectionForm.setDocuments(documents);

        String articleType = documentsSelectionForm.getDocuments().size()==1 ? ArticleType.SINGLE_DOC.name() : ArticleType.MULTIPLE_DOCS.name();

        GenerateArticleResponse generateArticleResponse = this.articleService.generateArticle(
                documentsSelectionForm.getDocuments().stream().map(DocumentResultEntry::getDocumentId).toList(),articleType,documentsSelectionForm.getTemplateId());

        model.addAttribute("articleContent", generateArticleResponse.getArticleContent());

        return "article";
    }

}
