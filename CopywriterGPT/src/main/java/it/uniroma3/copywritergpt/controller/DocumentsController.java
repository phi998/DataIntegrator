package it.uniroma3.copywritergpt.controller;

import it.uniroma3.copywritergpt.controller.dto.DocumentsSearchQueryForm;
import it.uniroma3.copywritergpt.domain.PromptTemplateService;
import it.uniroma3.copywritergpt.service.DocumentsService;
import it.uniroma3.copywritergpt.service.OntologyService;
import it.uniroma3.di.common.api.dto.copywritergpt.DocumentsSelectionForm;
import it.uniroma3.di.common.api.dto.copywritergpt.GetPromptTemplateResponse;
import it.uniroma3.di.common.api.dto.dim.GetOntologyCollectionResponse;
import it.uniroma3.di.common.api.dto.dim.GetOntologyResponse;
import it.uniroma3.di.common.api.dto.tss.QueryResponse;
import it.uniroma3.di.common.api.dto.tss.ResultEntryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import static it.uniroma3.di.common.utils.Constants.*;

@Controller
@Slf4j
public class DocumentsController {

    private final static String ATTR_KEYVAL_SEP = ":";

    private final static String ATTR_LIST_SEP = ",";

    private static final int DEFAULT_N_RESULTS = 20;

    @Autowired
    private DocumentsService documentsService;

    @Autowired
    private PromptTemplateService promptTemplateService;

    @Autowired
    private OntologyService ontologyService;

    @GetMapping("/query")
    public String getTopResults(
            @RequestParam Map<String,String> queryParams, Model model) {
        log.info("getTopResults(): queryParams={}", queryParams);

        int nResults = DEFAULT_N_RESULTS;
        if(queryParams.containsKey(TSS_N_RESULTS_KEY)) {
            nResults = Integer.parseInt(queryParams.get(TSS_N_RESULTS_KEY));
            queryParams.remove(TSS_N_RESULTS_KEY);
        }

        String collectionName = null;
        if(queryParams.containsKey(TSS_TABLE_NAME_KEY)) {
            collectionName = queryParams.get(TSS_TABLE_NAME_KEY);
            queryParams.remove(TSS_TABLE_NAME_KEY);
        }

        Long templateId = queryParams.containsKey(TEMPLATE_ID_KEY)? Long.parseLong(queryParams.get(TEMPLATE_ID_KEY)):0;
        queryParams.remove(TEMPLATE_ID_KEY);

        /*
        GetPromptTemplateResponse promptTemplateResponse = this.promptTemplateService.getPromptTemplateResponse(templateId);
        model.addAttribute("promptTemplate", promptTemplateResponse);*/

        List<String> ontologies = ontologyService.getOntologies(null).getOntologies().stream().map(GetOntologyResponse::getName).toList();
        if(collectionName == null || collectionName.trim().equals(""))
            collectionName = ontologies.get(new Random().nextInt(ontologies.size()));

        GetOntologyResponse ontologyDetails = ontologyService.getOntologies(collectionName).getOntologies().get(0);

        Map<String,List<String>> queryParamsLists = new HashMap<>();
        queryParams.forEach((k,v) -> queryParamsLists.put(k, List.of(v.split(ATTR_LIST_SEP))));
        QueryResponse queryResponse = documentsService.getTopResults(collectionName, queryParamsLists, nResults);

        DocumentsSelectionForm documentsSelectionForm = new DocumentsSelectionForm();
        documentsSelectionForm.setTemplateId(templateId);
        documentsSelectionForm.setCollectionName(collectionName);
        Collection<ResultEntryResponse> documents = queryResponse.getDocuments();
        for(ResultEntryResponse document: documents) {
            documentsSelectionForm.addDocument(document.getColumnName2Content().get("id"), document.getColumnName2Content());
        }

        DocumentsSearchQueryForm documentsSearchQueryForm = new DocumentsSearchQueryForm();
        documentsSearchQueryForm.setTemplateId(templateId);
        ontologyDetails.getItems().forEach(
                item -> documentsSearchQueryForm.addQueryStringItem(item.getLabel(), "")
        );

        model.addAttribute("documentsForm", documentsSelectionForm);
        model.addAttribute("documentsSearchQueryForm", documentsSearchQueryForm);
        model.addAttribute("collectionNames", ontologies);
        // Note: the document id is stored inside the document itself

        return "documents";
    }

    @PostMapping("/query")
    public String getDocuments(@ModelAttribute("documentsSearchQueryForm") DocumentsSearchQueryForm documentsSearchQueryForm,
                               RedirectAttributes redirectAttributes) {
        log.info("getDocuments(): documentsSearchQueryForm={}", documentsSearchQueryForm);

        redirectAttributes.addAttribute(TEMPLATE_ID_KEY, String.valueOf(documentsSearchQueryForm.getTemplateId()));
        redirectAttributes.addAttribute(TSS_TABLE_NAME_KEY, documentsSearchQueryForm.getCollectionName());
        redirectAttributes.addAttribute(WILDCARD_KEY, documentsSearchQueryForm.getQueryString());

        for(Map.Entry<String,String> queryItem: documentsSearchQueryForm.getAdvancedQueryString().entrySet()) {
            if(!queryItem.getValue().equals(""))
                redirectAttributes.addAttribute(queryItem.getKey(), queryItem.getValue());
        }

        return "redirect:/query";
    }


}
