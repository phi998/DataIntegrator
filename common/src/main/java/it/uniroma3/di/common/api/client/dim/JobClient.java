package it.uniroma3.di.common.api.client.dim;

import it.uniroma3.di.common.api.client.Client;
import it.uniroma3.di.common.api.dto.dim.*;
import it.uniroma3.di.common.utils.Endpoints;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class JobClient extends Client {

    private static final String JOB_ENDPOINT = Endpoints.DIM_ENDPOINT + "/jobs";

    public JobClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public CreateNewJobResponse createNewJob(CreateNewJobRequest createNewJobRequest) {
        String url = JOB_ENDPOINT;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<CreateNewJobResponse> responseEntity =
                restTemplate.postForEntity(url, createNewJobRequest, CreateNewJobResponse.class);

        return responseEntity.getBody();
    }

    public TableAddedToJobResponse addTableToJob(Long jobId, AddTableToJobRequest addTableToJobRequest) {
        String url = JOB_ENDPOINT + "/" + jobId + "/tables";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<TableAddedToJobResponse> responseEntity =
                restTemplate.postForEntity(url, addTableToJobRequest, TableAddedToJobResponse.class);

        return responseEntity.getBody();
    }

    public GetJobInfoResponse getJob(Long jobId, boolean showTables) {
        String url = JOB_ENDPOINT + "/" + jobId + "?showTables=" + showTables;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<GetJobInfoResponse> responseEntity =
                restTemplate.getForEntity(url, GetJobInfoResponse.class);

        return responseEntity.getBody();
    }

    public GetJobListResponse getAllJobs() {
        String url = JOB_ENDPOINT;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<GetJobListResponse> responseEntity =
                restTemplate.getForEntity(url, GetJobListResponse.class);

        return responseEntity.getBody();
    }

    public JobStartedResponse startJob(Long jobId, StartJobRequest startJobRequest) {
        String url = JOB_ENDPOINT + "/" + jobId + "/start";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<JobStartedResponse> responseEntity =
                restTemplate.postForEntity(url, startJobRequest, JobStartedResponse.class);

        return responseEntity.getBody();
    }

    public TablesPreviewResponse getPreview(Long jobId, int rows) {
        String url = JOB_ENDPOINT + "/" + jobId + "/preview?rows="+rows;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<TablesPreviewResponse> responseEntity =
                restTemplate.getForEntity(url, TablesPreviewResponse.class);

        return responseEntity.getBody();
    }

    public void renameColumns(Long jobId, EditTableColumnsNamesRequest editTableColumnsNamesRequest) {
        String url = JOB_ENDPOINT + "/" + jobId + "/preview";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.put(url, editTableColumnsNamesRequest, Void.class);
    }

    public void pushJob(Long jobId) {
        String url = JOB_ENDPOINT + "/" + jobId + "/push";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.postForEntity(url, null, Void.class);
    }
}
