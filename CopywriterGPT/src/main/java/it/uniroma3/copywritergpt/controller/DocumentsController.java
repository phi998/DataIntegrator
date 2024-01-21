package it.uniroma3.copywritergpt.controller;

import it.uniroma3.copywritergpt.service.DocumentsService;
import it.uniroma3.di.common.api.dto.tss.QueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static it.uniroma3.di.common.utils.Constants.TSS_N_RESULTS_KEY;

@Controller
@Slf4j
public class DocumentsController {

    @Autowired
    private DocumentsService documentsService;

    @GetMapping("/query")
    public String getTopResults(
            @RequestParam Map<String,String> queryParams,
            Model model) {
        log.info("getTopResults(): queryParams={}", queryParams);

        int nResults = Integer.parseInt(queryParams.get(TSS_N_RESULTS_KEY));
        queryParams.remove(TSS_N_RESULTS_KEY);

        if(queryParams.isEmpty())
            queryParams.put("*","*");

        QueryResponse queryResponse = documentsService.getTopResults(queryParams, nResults);

        model.addAttribute("documents", queryResponse.getDocuments());
        // Note: the document id is stored inside the document itself

        return "documents";
    }

}
