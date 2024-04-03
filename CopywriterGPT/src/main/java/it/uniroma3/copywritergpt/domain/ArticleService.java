package it.uniroma3.copywritergpt.domain;

import it.uniroma3.copywritergpt.domain.entity.PromptTemplate;
import it.uniroma3.copywritergpt.domain.vo.Document;
import it.uniroma3.copywritergpt.engine.ArticleGenerator;
import it.uniroma3.copywritergpt.engine.TemplateFiller;
import it.uniroma3.copywritergpt.enums.ArticleType;
import it.uniroma3.copywritergpt.proxy.DimProxy;
import it.uniroma3.di.common.api.dto.copywritergpt.GenerateArticleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ArticleService {

    private DimProxy dimProxy;

    @Autowired
    private PromptTemplateService promptTemplateService;

    @Autowired
    private ArticleRepository articleRepository;

    public ArticleService() {
        this.dimProxy = new DimProxy(new RestTemplate());
    }

    public GenerateArticleResponse generateArticle(String collectionName, List<String> documentIds, String articleType, Long promptTemplateId) {
        log.info("generateArticle(): documentIds={}, promptTemplateId={}", documentIds, promptTemplateId);

        List<Document> documents = dimProxy.getDocumentsById(collectionName, documentIds);

        PromptTemplate promptTemplate = promptTemplateService.getPromptTemplateById(promptTemplateId);

        TemplateFiller templateFiller = new TemplateFiller();
        String prompt = templateFiller.fillTemplate(promptTemplate.getContent(), documents, ArticleType.valueOf(articleType));

        ArticleGenerator articleGenerator = new ArticleGenerator();
        String articleContent = articleGenerator.generateArticle(promptTemplate.getCategory().getName(), prompt);

        GenerateArticleResponse generateArticleResponse = new GenerateArticleResponse();
        generateArticleResponse.setArticleContent(articleContent);

        log.info("generateArticle(): generateArticleResponse={}", generateArticleResponse);

        return generateArticleResponse;
    }

}
