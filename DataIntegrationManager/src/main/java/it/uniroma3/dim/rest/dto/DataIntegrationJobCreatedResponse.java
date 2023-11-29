package it.uniroma3.dim.rest.dto;

import lombok.Data;

@Data
public class DataIntegrationJobCreatedResponse {

    private Long jobId;

    private String jobName;

    private String jobStatus;

}
