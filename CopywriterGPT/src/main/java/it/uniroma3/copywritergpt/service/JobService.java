package it.uniroma3.copywritergpt.service;

import it.uniroma3.di.common.api.client.dim.JobClient;
import it.uniroma3.di.common.api.dto.dim.CreateNewJobRequest;
import it.uniroma3.di.common.api.dto.dim.CreateNewJobResponse;
import it.uniroma3.di.common.api.dto.dim.CreateNewOntologyRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class JobService {

    private JobClient jobClient;

    public JobService() {
        this.jobClient = new JobClient(new RestTemplate());
    }

    public CreateNewJobResponse createNewJob(CreateNewJobRequest createNewJobRequest) {
        log.info("createNewJob(): createNewJobRequest={}", createNewJobRequest);

        return this.jobClient.createNewJob(createNewJobRequest);
    }

}
