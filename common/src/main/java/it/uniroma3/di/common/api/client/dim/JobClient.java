package it.uniroma3.di.common.api.client.dim;

import it.uniroma3.di.common.api.client.Client;
import it.uniroma3.di.common.api.dto.dim.CreateNewJobRequest;
import it.uniroma3.di.common.api.dto.dim.CreateNewJobResponse;
import it.uniroma3.di.common.api.dto.dim.CreateNewOntologyResponse;
import it.uniroma3.di.common.utils.Endpoints;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

}
