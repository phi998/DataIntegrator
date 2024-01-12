package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

@Data
public class DataIntegrationJobCreatedResponse {

    private Long jobId;

    private String jobName;

    private String jobStatus;

}
