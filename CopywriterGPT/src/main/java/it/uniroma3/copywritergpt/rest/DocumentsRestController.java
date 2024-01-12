package it.uniroma3.copywritergpt.rest;

import it.uniroma3.copywritergpt.domain.ApplicationService;
import it.uniroma3.copywritergpt.domain.PromptTemplateService;
import it.uniroma3.copywritergpt.domain.entity.PromptTemplate;
import it.uniroma3.copywritergpt.domain.vo.Document;
import it.uniroma3.copywritergpt.engine.ArticleGenerator;
import it.uniroma3.copywritergpt.engine.TemplateFiller;
import it.uniroma3.copywritergpt.enums.ArticleType;
import it.uniroma3.copywritergpt.proxy.TssProxy;
import it.uniroma3.di.common.api.dto.copywritergpt.GenerateArticleResponse;
import it.uniroma3.di.common.api.dto.tss.QueryResponse;
import it.uniroma3.di.common.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class DocumentsRestController {

    @Autowired
    private PromptTemplateService promptTemplateService;

    @GetMapping("/collection/{collectionName}/documents")
    public QueryResponse getDocuments(@PathVariable String collectionName, @RequestParam Map<String,String> queryParams) {
        log.info("getDocuments(): collectionName={}, queryParams={}", collectionName, queryParams);
        TssProxy tssProxy = new TssProxy(new RestTemplate());

        int n = Integer.parseInt(queryParams.get(Constants.TSS_N_RESULTS_KEY));
        queryParams.remove(Constants.TSS_N_RESULTS_KEY);

        return tssProxy.getTopResults(queryParams,collectionName,n);
    }

    @GetMapping("/collection/{collectionName}/article")
    public GenerateArticleResponse generateArticle(
            @RequestParam Long templateId,
            @RequestParam String articleType,
            @RequestParam List<String> documentIds,
            @PathVariable String collectionName) {
        log.info("generateArticle(): collectionName={}, templateId?{}, articleType={}, documentIds={}, ",
                collectionName, templateId, articleType, documentIds);

        TssProxy tssProxy = new TssProxy(new RestTemplate());

        List<Document> documents = tssProxy.getDocumentsById(collectionName,documentIds);

        PromptTemplate promptTemplate = promptTemplateService.getPromptTemplateById(templateId);

        TemplateFiller templateFiller = new TemplateFiller();
        String prompt = templateFiller.fillTemplate(promptTemplate.getContent(), documents, ArticleType.valueOf(articleType));

        ArticleGenerator articleGenerator = new ArticleGenerator();
        String articleContent = articleGenerator.generateArticle(collectionName, prompt);

        GenerateArticleResponse generateArticleResponse = new GenerateArticleResponse();
        generateArticleResponse.setArticleContent(articleContent);

        return generateArticleResponse;
    }

}
