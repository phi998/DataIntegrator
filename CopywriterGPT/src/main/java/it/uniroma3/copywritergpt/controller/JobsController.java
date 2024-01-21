package it.uniroma3.copywritergpt.controller;

import it.uniroma3.copywritergpt.controller.dto.NewJobForm;
import it.uniroma3.copywritergpt.controller.dto.PreviewRenameColumnsForm;
import it.uniroma3.copywritergpt.controller.dto.RenameTableColumnsForm;
import it.uniroma3.copywritergpt.controller.dto.StartJobForm;
import it.uniroma3.copywritergpt.service.JobService;
import it.uniroma3.copywritergpt.service.OntologyService;
import it.uniroma3.copywritergpt.utils.Util;
import it.uniroma3.di.common.api.dto.dim.*;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Controller
@Slf4j
public class JobsController {

    @Autowired
    private JobService jobService;

    @Autowired
    private OntologyService ontologyService;

    @GetMapping("/jobs")
    public String getJobsList(
            Model model) {
        log.info("getJobsList()");

        GetJobListResponse getJobListResponse = this.jobService.getAllJobs();
        model.addAttribute("jobs", getJobListResponse);

        GetOntologyCollectionResponse getOntologyCollectionResponse = ontologyService.getOntologies(null);
        model.addAttribute("ontologies", getOntologyCollectionResponse);

        model.addAttribute("jobForm", new NewJobForm());

        return "jobs";
    }

    @PostMapping("/jobs")
    public String createNewJob(
            @ModelAttribute("jobForm") NewJobForm newJobForm,
            @RequestParam("files") List<MultipartFile> files,
            Model model) {
        log.info("createNewJob(): newJobForm={}", newJobForm);
        List<AddTableToJobRequest> tables = new ArrayList<>();

        if(newJobForm.getJobType() == null || newJobForm.getJobType().equals(""))
            newJobForm.setJobType("COLUMN_NAMING");

        for (MultipartFile file : files) {
            log.info("createNewJob(): Received file={}", file.getOriginalFilename());
            AddTableToJobRequest table = new AddTableToJobRequest();
            table.setTableName(file.getName());
            try {
                String csvStringContent = new String(file.getBytes(), StandardCharsets.UTF_8);
                table.setTableContent(Util.convertToBase64String(csvStringContent));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            tables.add(table);
        }

        CreateNewJobRequest createNewJobRequest = new CreateNewJobRequest();
        createNewJobRequest.setJobName(newJobForm.getJobName());
        createNewJobRequest.setJobType(newJobForm.getJobType());
        createNewJobRequest.setOntologyName(newJobForm.getOntologyName());

        CreateNewJobResponse response = jobService.createNewJob(createNewJobRequest, tables);
        return "redirect:jobs/" + response.getJobId();
    }

    @GetMapping("/jobs/{jobId}")
    public String getJobInfo(
            @PathVariable("jobId") Long jobId,
            @PathParam("showTables") boolean showTables,
            Model model) {
        log.info("getJobInfo(): jobId={}", jobId);

        GetJobInfoResponse jobInfo = this.jobService.getJobInfo(jobId, showTables);

        final int N_PREVIEW_ROWS = 10;
        TablesPreviewResponse preview = this.jobService.getTablesPreview(jobId, N_PREVIEW_ROWS);

        if(!preview.getTables().isEmpty()) {
            model.addAttribute("tables", preview.getTables());
        } else {
            model.addAttribute("tables", new HashMap<String, TablePreview>());
        }

        model.addAttribute("startJobForm", new StartJobForm());

        model.addAttribute("job", jobInfo);

        return "job";
    }

    @PostMapping("/jobs/{jobId}/start")
    public String startJob(
            @PathVariable("jobId") Long jobId,
            @ModelAttribute("startJobForm") StartJobForm startJobForm,
            Model model) {
        log.info("startJob(): jobId={}", jobId);

        List<Integer> columnsToDrop = startJobForm.getColumnsToDrop().stream().map(Integer::parseInt).toList();

        this.jobService.startJob(jobId, columnsToDrop);

        return "redirect:jobs/" + jobId;
    }

    @PostMapping("/jobs/{jobId}/push")
    public String pushJob(
            @PathVariable("jobId") Long jobId,
            @ModelAttribute("previewForm") PreviewRenameColumnsForm previewRenameColumnsForm,
            Model model) {
        log.info("pushJob(): jobId={}", jobId);

        EditTableColumnsNamesRequest editTableColumnsNamesRequest = new EditTableColumnsNamesRequest();
        previewRenameColumnsForm.getTableName2newColumnsNames().forEach((tableName,entry) -> {
            RenameTableColumnsForm renameTableColumnsForm = entry;
            Collection<String> colNames = new ArrayList<>(renameTableColumnsForm.getColIndex2newName().values());

            editTableColumnsNamesRequest.addTable(tableName,colNames);
        });

        this.jobService.renameColumns(jobId, editTableColumnsNamesRequest);
        this.jobService.pushJob(jobId);

        return "redirect:jobs/" + jobId;
    }

}
