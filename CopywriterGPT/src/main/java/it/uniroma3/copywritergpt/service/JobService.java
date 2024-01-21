package it.uniroma3.copywritergpt.service;

import it.uniroma3.di.common.api.client.dim.JobClient;
import it.uniroma3.di.common.api.dto.dim.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class JobService {

    private JobClient jobClient;

    public JobService() {
        this.jobClient = new JobClient(new RestTemplate());
    }

    public CreateNewJobResponse createNewJob(CreateNewJobRequest createNewJobRequest, List<AddTableToJobRequest> tables) {
        log.info("createNewJob(): createNewJobRequest={}", createNewJobRequest);

        CreateNewJobResponse createNewJobResponse = this.jobClient.createNewJob(createNewJobRequest);

        for(AddTableToJobRequest table: tables) {
            this.jobClient.addTableToJob(createNewJobResponse.getJobId(), table);
        }

        return createNewJobResponse;
    }

    public GetJobInfoResponse getJobInfo(Long jobId, boolean showTables) {
        log.info("getJobInfo(): jobId={}", jobId);

        return this.jobClient.getJob(jobId, showTables);
    }

    public GetJobListResponse getAllJobs() {
        log.info("getALlJobs()");

        return this.jobClient.getAllJobs();
    }

    public JobStartedResponse startJob(Long jobId, List<Integer> columnsToDrop) {
        log.info("startJob(): jobId={}, columnsToDrop={}", jobId, columnsToDrop);

        StartJobRequest startJobRequest = new StartJobRequest();
        startJobRequest.setColumnsToDrop(columnsToDrop);

        return this.jobClient.startJob(jobId, startJobRequest);
    }

    public TablesPreviewResponse getTablesPreview(Long jobId, int rows) {
        log.info("getTablesPreview(): jobId={}, rows={}", jobId, rows);

        return this.jobClient.getPreview(jobId, rows);
    }

    public void renameColumns(Long jobId, EditTableColumnsNamesRequest editTableColumnsNamesRequest) {
        log.info("renameColumns(): editTableColumnsNamesRequest={}", editTableColumnsNamesRequest);

        this.jobClient.renameColumns(jobId, editTableColumnsNamesRequest);
    }

    public void pushJob(Long jobId) {
        log.info("pushJob(): jobId={}", jobId);

        this.jobClient.pushJob(jobId);
    }


}
