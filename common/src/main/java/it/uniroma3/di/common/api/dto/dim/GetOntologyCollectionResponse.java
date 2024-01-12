package it.uniroma3.di.common.api.dto.dim;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetOntologyCollectionResponse {

    private List<GetOntologyResponse> ontologies;

    public GetOntologyCollectionResponse() {
        this.ontologies = new ArrayList<>();
    }

    public void addOntology(GetOntologyResponse getOntologyResponse) {
        this.ontologies.add(getOntologyResponse);
    }

}
