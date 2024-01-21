package it.uniroma3.di.common.api.client.dim;

import it.uniroma3.di.common.api.client.Client;
import it.uniroma3.di.common.api.dto.dim.*;
import it.uniroma3.di.common.utils.Endpoints;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class OntologyClient extends Client {

    private static final String ONTOLOGY_ENDPOINT = Endpoints.DIM_ENDPOINT + "/ontology";

    public OntologyClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public CreateNewOntologyResponse createNewOntology(CreateNewOntologyRequest createNewOntologyRequest) {
        String url = ONTOLOGY_ENDPOINT;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<CreateNewOntologyResponse> responseEntity =
                restTemplate.postForEntity(url, createNewOntologyRequest, CreateNewOntologyResponse.class);

        return responseEntity.getBody();
    }

    public GetOntologyCollectionResponse getOntologies(String name) {
        String url = ONTOLOGY_ENDPOINT;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParam("name", name);
        url = builder.build().toUriString();

        ResponseEntity<GetOntologyCollectionResponse> responseEntity =
                restTemplate.getForEntity(url, GetOntologyCollectionResponse.class);

        return responseEntity.getBody();
    }

    public GetOntologyResponse getOntologyById(Long ontologyId) {
        String url = ONTOLOGY_ENDPOINT + "/" + ontologyId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<GetOntologyResponse> responseEntity =
                restTemplate.getForEntity(url, GetOntologyResponse.class);

        return responseEntity.getBody();
    }

    public void addItemsToOntology(Long ontologyId, AddItemsToOntologyRequest addItemsToOntologyRequest) {
        String url = ONTOLOGY_ENDPOINT + "/" + ontologyId + "/items";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.postForEntity(url, addItemsToOntologyRequest, Void.class);
    }


}
