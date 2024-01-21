package it.uniroma3.copywritergpt.controller.dto;

import lombok.Data;

@Data
public class NewJobForm {

    private String jobName;

    private String ontologyName;

    private String jobType;

}
