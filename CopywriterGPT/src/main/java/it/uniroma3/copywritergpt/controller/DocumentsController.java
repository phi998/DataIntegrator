package it.uniroma3.copywritergpt.controller;

import it.uniroma3.copywritergpt.controller.dto.DocumentsSearchQueryForm;
import it.uniroma3.copywritergpt.domain.PromptTemplateService;
import it.uniroma3.copywritergpt.service.DocumentsService;
import it.uniroma3.di.common.api.dto.copywritergpt.DocumentsSelectionForm;
import it.uniroma3.di.common.api.dto.copywritergpt.GetPromptTemplateResponse;
import it.uniroma3.di.common.api.dto.tss.QueryResponse;
import it.uniroma3.di.common.api.dto.tss.ResultEntryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static it.uniroma3.di.common.utils.Constants.*;

@Controller
@Slf4j
public class DocumentsController {

    private static final int DEFAULT_N_RESULTS = 20;

    @Autowired
    private DocumentsService documentsService;

    @Autowired
    private PromptTemplateService promptTemplateService;

    @GetMapping("/query")
    public String getTopResults(
            @RequestParam Map<String,String> queryParams, Model model) {
        log.info("getTopResults(): queryParams={}", queryParams);

        int nResults = DEFAULT_N_RESULTS;
        if(queryParams.containsKey(TSS_N_RESULTS_KEY)) {
            nResults = Integer.parseInt(queryParams.get(TSS_N_RESULTS_KEY));
            queryParams.remove(TSS_N_RESULTS_KEY);
        }

        Long templateId = Long.parseLong(queryParams.get(TEMPLATE_ID_KEY));
        queryParams.remove(TEMPLATE_ID_KEY);

        GetPromptTemplateResponse promptTemplateResponse = this.promptTemplateService.getPromptTemplateResponse(templateId);

        model.addAttribute("promptTemplate", promptTemplateResponse);

        if(queryParams.isEmpty())
            queryParams.put("*","*");

        QueryResponse queryResponse = documentsService.getTopResults(queryParams, nResults);

        DocumentsSelectionForm documentsSelectionForm = new DocumentsSelectionForm();
        documentsSelectionForm.setTemplateId(templateId);
        Collection<ResultEntryResponse> documents = queryResponse.getDocuments();
        for(ResultEntryResponse document: documents) {
            documentsSelectionForm.addDocument(document.getColumnName2Content().get("id"), document.getColumnName2Content());
        }

        model.addAttribute("documentsForm", documentsSelectionForm);
        model.addAttribute("documentsSearchQueryForm", new DocumentsSearchQueryForm());
        // Note: the document id is stored inside the document itself

        return "documents";
    }

    @PostMapping("/query")
    public String getDocuments(@ModelAttribute("documentsSearchQueryForm") DocumentsSearchQueryForm documentsSearchQueryForm,
                               RedirectAttributes redirectAttributes) {
        log.info("getDocuments(): documentsSearchQueryForm={}", documentsSearchQueryForm);

        String[] params = documentsSearchQueryForm.getQueryString().split("and");

        redirectAttributes.addAttribute("templateId", String.valueOf(documentsSearchQueryForm.getTemplateId()));

        for(String param :params) {
            param = param.trim();
            redirectAttributes.addAttribute(param.split("=")[0], param.split("=")[1]);
        }

        return "redirect:/query";
    }


}
