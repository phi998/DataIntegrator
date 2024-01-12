package it.uniroma3.dim.rest;


import it.uniroma3.di.common.api.dto.dim.*;
import it.uniroma3.dim.domain.OntologyService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class DataOntologyRestController {

    @Autowired
    private OntologyService ontologyService;

    @GetMapping("/ontology/{ontologyId}")
    public GetOntologyResponse getOntologyInformation(
            @PathVariable Long ontologyId) {
        log.info("getOntologyInformation(): ontologyId={}", ontologyId);
        return ontologyService.getOntologyInformation(ontologyId);
    }

    @PostMapping("/ontology")
    public CreateNewOntologyResponse createNewOntologyResponse(
            @RequestBody CreateNewOntologyRequest createNewOntologyRequest) {
        log.info("createNewOntologyResponse(): createNewOntologyRequest={}", createNewOntologyRequest);

        return ontologyService.createNewOntology(createNewOntologyRequest.getName(), createNewOntologyRequest.getItems());
    }

    @GetMapping("/ontology")
    public GetOntologyCollectionResponse getOntologies(@PathParam("name") String name) {
        log.info("getOntologies(): name={}", name);

        return ontologyService.getOntologies(name);
    }

    @PostMapping("/ontology/{ontologyId}/items")
    public void addItemsToOntology(@PathVariable Long ontologyId,
            @RequestBody AddItemsToOntologyRequest addItemsToOntologyRequest) {
        log.info("addItemsToOntology(): ontologyId={}, addItemsToOntologyRequest={}",ontologyId,addItemsToOntologyRequest);

        ontologyService.addItemsToOntology(ontologyId, addItemsToOntologyRequest.getOntologyItemsInput());
    }

}
