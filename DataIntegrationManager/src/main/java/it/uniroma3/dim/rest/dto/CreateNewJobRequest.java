package it.uniroma3.dim.rest.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class CreateNewJobRequest {

    private String jobName;

    private Collection<String> ontology;

}
