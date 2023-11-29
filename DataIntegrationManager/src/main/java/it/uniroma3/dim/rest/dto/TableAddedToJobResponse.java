package it.uniroma3.dim.rest.dto;

import lombok.Data;

@Data
public class TableAddedToJobResponse {

    private Long jobId;

    private String jobName;

}
