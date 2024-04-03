package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class AddItemsToOntologyRequest {

    private List<OntologyItemInput> ontologyItemsInput;

    public AddItemsToOntologyRequest() {
        this.ontologyItemsInput = new ArrayList<>();
    }

}
