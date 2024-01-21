package it.uniroma3.copywritergpt.service;

import it.uniroma3.di.common.api.client.dim.OntologyClient;
import it.uniroma3.di.common.api.dto.dim.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OntologyService {

    private OntologyClient ontologyClient;

    public OntologyService() {
        this.ontologyClient = new OntologyClient(new RestTemplate());
    }

    public CreateNewOntologyResponse createNewOntology(CreateNewOntologyRequest createNewOntologyRequest) {
        log.info("createNewOntology(): createNewOntologyRequest={}", createNewOntologyRequest);

        return this.ontologyClient.createNewOntology(createNewOntologyRequest);
    }

    public GetOntologyCollectionResponse getOntologies(String name) {
        log.info("getOntologies(): name={}", name);

        return this.ontologyClient.getOntologies(name);
    }

    public GetOntologyResponse getOntologyById(Long ontologyId) {
        log.info("getOntologyById(): ontologyId={}", ontologyId);

        return this.ontologyClient.getOntologyById(ontologyId);
    }

    public void addItemsToOntology(Long ontologyId, AddItemsToOntologyRequest addItemsToOntologyRequest) {
        log.info("addItemsToOntology(): ontologyId={}, addItemsToOntologyRequest={}", ontologyId, addItemsToOntologyRequest);

        this.ontologyClient.addItemsToOntology(ontologyId, addItemsToOntologyRequest);
    }

}
