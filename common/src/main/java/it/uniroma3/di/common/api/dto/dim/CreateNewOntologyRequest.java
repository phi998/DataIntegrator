package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class CreateNewOntologyRequest {

    private String name;

    private Collection<OntologyItemInput> items;

    public CreateNewOntologyRequest() {
        this.items = new ArrayList<>();
    }

}
