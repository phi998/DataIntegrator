package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class GetOntologyResponse {

    private Long id;

    private String name;

    private Collection<OntologyItemOutput> items;

    public GetOntologyResponse() {
        this.items = new ArrayList<>();
    }

    public void addItem(OntologyItemOutput item) {
        this.items.add(item);
    }

}
