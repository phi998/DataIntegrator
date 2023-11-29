package it.uniroma3.dim.rest.dto;

import lombok.Data;

@Data
public class GetJobInfoResponse {

    private Long id;

    private String name;

    private String jobStatus;

}
