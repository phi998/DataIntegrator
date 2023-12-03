package it.uniroma3.dim.rest;

import it.uniroma3.dim.domain.JobService;
import it.uniroma3.dim.rest.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@Slf4j
public class DataIntegratorRestController {


    @Autowired
    private JobService jobService;

    @PostMapping("/jobs")
    public CreateNewJobResponse createNewJob(
        @RequestBody CreateNewJobRequest newJobRequest
    ) {
        log.info("createNewJob(): newJobRequest={}", newJobRequest);

        return jobService.createNewJob(newJobRequest.getJobName(), newJobRequest.getOntology(), newJobRequest.getJobType());
    }

    @GetMapping("/jobs/{jobId}")
    public GetJobInfoResponse getJobInfo(@PathVariable Long jobId, @RequestParam(name = "showTables", defaultValue = "false") boolean showTables) {
        log.info("getJobInfo(): jobId={}", jobId);

        GetJobInfoResponse jobInfoResponse = jobService.getJobInfo(jobId, showTables);
        log.info("getJobInfo(): jobInfoResponse={}", jobInfoResponse);

        return jobInfoResponse;
    }

    @PostMapping("/jobs/{jobId}/tables")
    public TableAddedToJobResponse addTableToJob(
            @PathVariable Long jobId,
            @RequestBody AddTableToJobRequest addTableToJobRequest
            ) {
        log.info("addTableToJob(): jobId={}, tableName={}",jobId,addTableToJobRequest.getTableName());

        String tableContent = new String(Base64.getDecoder().decode(addTableToJobRequest.getTableContent()), StandardCharsets.UTF_8);
        return jobService.addNewTableToJob(jobId, addTableToJobRequest.getTableName(), tableContent);
    }

    @PostMapping("/jobs/{jobId}/start")
    public JobStartedResponse startJob(
            @PathVariable Long jobId
    ) {
        log.info("startJob(): jobId={}",jobId);
        return jobService.startJob(jobId);
    }



}
