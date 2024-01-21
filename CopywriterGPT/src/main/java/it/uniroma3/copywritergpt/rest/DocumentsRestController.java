package it.uniroma3.copywritergpt.rest;

import it.uniroma3.copywritergpt.domain.PromptTemplateService;
import it.uniroma3.copywritergpt.service.DocumentsService;
import it.uniroma3.di.common.api.dto.tss.QueryResponse;
import it.uniroma3.di.common.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class DocumentsRestController {

    @Autowired
    private PromptTemplateService promptTemplateService;

    @Autowired
    private DocumentsService documentsService;

    @GetMapping("/collection/{collectionName}/documents")
    public QueryResponse getDocuments(@PathVariable String collectionName, @RequestParam Map<String,String> queryParams) {
        log.info("getDocuments(): collectionName={}, queryParams={}", collectionName, queryParams);

        int n = Integer.parseInt(queryParams.get(Constants.TSS_N_RESULTS_KEY));
        queryParams.remove(Constants.TSS_N_RESULTS_KEY);

        return documentsService.getTopResults(queryParams,n);
    }

}
