package it.uniroma3.dim.rest.dto;

import lombok.Data;

@Data
public class CreateNewJobResponse {

    private Long jobId;

    private String jobName;

    private String jobStatus;

}
