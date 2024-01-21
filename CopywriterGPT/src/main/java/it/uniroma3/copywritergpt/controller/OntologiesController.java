package it.uniroma3.copywritergpt.controller;

import it.uniroma3.copywritergpt.controller.dto.AddOntologyItemsForm;
import it.uniroma3.copywritergpt.controller.dto.NewOntologyForm;
import it.uniroma3.copywritergpt.controller.dto.NewOntologyItem;
import it.uniroma3.copywritergpt.service.OntologyService;
import it.uniroma3.di.common.api.dto.copywritergpt.enums.OntologyItemType;
import it.uniroma3.di.common.api.dto.dim.*;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class OntologiesController {

    @Autowired
    private OntologyService ontologyService;

    @GetMapping("/ontology/{ontologyId}")
    public String getOntology(
            @PathVariable("ontologyId") Long ontologyId,
            Model model) {
        log.info("getOntologies(): ontologyId={}", ontologyId);

        GetOntologyResponse ontology = this.ontologyService.getOntologyById(ontologyId);

        model.addAttribute("itemForm", new NewOntologyItem());
        model.addAttribute("importanceValues", Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        model.addAttribute("itemTypes", OntologyItemType.values());
        model.addAttribute("ontology", ontology);

        return "ontology";
    }

    @PostMapping("/ontology")
    public String createNewOntology(
            @ModelAttribute("ontologyForm") NewOntologyForm newOntologyForm,
            Model model) {
        log.info("createNewOntology(): newOntologyForm={}", newOntologyForm);

        CreateNewOntologyRequest createNewOntologyRequest = new CreateNewOntologyRequest();
        createNewOntologyRequest.setName(newOntologyForm.getOntologyName());
        CreateNewOntologyResponse createNewOntologyResponse = ontologyService.createNewOntology(createNewOntologyRequest);

        return "redirect:/ontology/" + createNewOntologyResponse.getOntologyId();
    }

    @GetMapping("/ontology")
    public String getOntologies(
            @PathParam("name") String ontologyName,
            Model model) {
        log.info("getOntologies(): ontologyName={}", ontologyName);

        GetOntologyCollectionResponse getOntologyCollectionResponse = this.ontologyService.getOntologies(ontologyName);

        model.addAttribute("ontologyForm", new NewOntologyForm());
        model.addAttribute("ontologies", getOntologyCollectionResponse);

        return "ontologies";
    }

    @PostMapping("/ontology/{ontologyId}/items")
    public String addItemToOntology(
            @ModelAttribute("itemForm") NewOntologyItem newOntologyItem,
            @PathVariable("ontologyId") Long ontologyId,
            Model model) {
        log.info("addItemToOntology(): newOntologyItem={}, ontologyId={}", newOntologyItem, ontologyId);

        AddItemsToOntologyRequest addItemsToOntologyRequest = new AddItemsToOntologyRequest();
        OntologyItemInput ontologyItemInput = new OntologyItemInput();
        ontologyItemInput.setImportance(newOntologyItem.getImportance());
        ontologyItemInput.setType(newOntologyItem.getType());
        ontologyItemInput.setLabel(newOntologyItem.getLabel());
        ontologyItemInput.setNotes(newOntologyItem.getNotes());
        addItemsToOntologyRequest.getOntologyItemsInput().add(ontologyItemInput);
        
        this.ontologyService.addItemsToOntology(ontologyId, addItemsToOntologyRequest);

        return "redirect:/ontology";
    }


}
