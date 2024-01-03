package it.uniroma3.dim.rest.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateNewJobResponse {

    private Long jobId;

    private String jobName;

    private String jobStatus;

    private List<String> ontologyLabels;

    public CreateNewJobResponse() {
        this.ontologyLabels = new ArrayList<>();
    }

    public void addOntologyItemLabel(String label) {
        this.ontologyLabels.add(label);
    }

}
