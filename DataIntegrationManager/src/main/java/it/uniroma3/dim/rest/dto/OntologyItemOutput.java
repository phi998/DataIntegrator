package it.uniroma3.dim.rest.dto;

import lombok.Data;

@Data
public class OntologyItemOutput {

    private String label;

    private String type;

    private int importance; // a parameter that could be used for record linkage

    private String notes;

}
