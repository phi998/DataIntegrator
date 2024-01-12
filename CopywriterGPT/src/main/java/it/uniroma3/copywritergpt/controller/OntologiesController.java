package it.uniroma3.copywritergpt.controller;

import it.uniroma3.copywritergpt.service.OntologyService;
import it.uniroma3.di.common.api.dto.dim.CreateNewOntologyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OntologiesController {

    @Autowired
    private OntologyService ontologyService;

    @PostMapping("/ontology")
    public CreateNewOntologyResponse createNewOntology() {

        return null;
    }

}
