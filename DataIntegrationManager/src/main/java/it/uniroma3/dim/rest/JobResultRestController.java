package it.uniroma3.dim.rest;

import it.uniroma3.dim.domain.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class JobResultRestController {

    @Autowired
    private JobService jobService;

    @GetMapping("/jobs/{jobId}/preview")
    public Map<String, String> getTablesPreviews(@PathVariable("jobId") Long jobId) {
        log.info("getTablesPreviews(), jobId={}", jobId);
        return jobService.getEndedJobResultPreview(jobId);
    }

    public void editTableColumnNames() {

    }

    @PostMapping("/jobs/{jobId}/push")
    public void sendToTableStorageService(@PathVariable("jobId") Long jobId) {
        log.info("sendToTableStorageService(): jobId={}", jobId);
        jobService.push(jobId);
    }

}
