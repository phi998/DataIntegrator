package it.uniroma3.dim.rest.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class AddItemsToOntologyRequest {

    private Collection<OntologyItemInput> ontologyItemsInput;

    public AddItemsToOntologyRequest() {
        this.ontologyItemsInput = new ArrayList<>();
    }

}
