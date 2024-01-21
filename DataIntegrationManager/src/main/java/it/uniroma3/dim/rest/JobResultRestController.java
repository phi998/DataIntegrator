package it.uniroma3.dim.rest;

import it.uniroma3.di.common.api.dto.dim.GetEndedJobTablesResponse;
import it.uniroma3.dim.domain.JobService;
import it.uniroma3.di.common.api.dto.dim.EditTableColumnsNamesRequest;
import it.uniroma3.di.common.api.dto.dim.TablesPreviewResponse;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class JobResultRestController {

    @Autowired
    private JobService jobService;

    @GetMapping("/jobs/{jobId}/preview")
    public TablesPreviewResponse getTablesPreviews(@PathVariable("jobId") Long jobId, @PathParam("rows") int rows) {
        log.info("getTablesPreviews(), jobId={}, rows={}", jobId, rows);
        return jobService.getEndedJobResultPreview(jobId, rows);
    }

    @GetMapping("/jobs/{jobId}/result")
    public GetEndedJobTablesResponse getJobResult(@PathVariable("jobId") Long jobId, @PathParam("rows") int rows) {
        log.info("getTablesPreviews(), jobId={}, rows={}", jobId, rows);
        return jobService.getEndedJobResult(jobId);
    }

    @PutMapping("/jobs/{jobId}/preview")
    public void editTableColumnNames(@RequestBody EditTableColumnsNamesRequest editTableColumnsNamesRequest, @PathVariable Long jobId) {
        log.info("editTableColumnNames(): jobId={}, editTableColumnsNamesRequest={}", jobId, editTableColumnsNamesRequest);
        jobService.renameColumns(jobId, editTableColumnsNamesRequest.getTable2colnames());
    }

    @PostMapping("/jobs/{jobId}/push")
    public void sendToTableStorageService(@PathVariable("jobId") Long jobId) {
        log.info("sendToTableStorageService(): jobId={}", jobId);
        jobService.push(jobId);
    }

}
