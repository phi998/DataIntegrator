package it.uniroma3.dim.domain;

import it.uniroma3.di.common.api.dto.dim.*;
import it.uniroma3.dim.domain.entity.Ontology;
import it.uniroma3.dim.domain.entity.OntologyItem;
import it.uniroma3.dim.domain.enums.OntologyItemType;
import it.uniroma3.dim.proxy.TssStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class OntologyService {

    @Autowired
    private OntologyRepository ontologyRepository;

    @Autowired
    private StructureService structureService;

    @Transactional
    public CreateNewOntologyResponse createNewOntology(String ontologyName, Collection<OntologyItemInput> items) {
        log.info("createNewOntology(): ontologyName={}, items={}",ontologyName,items);

        Ontology ontology = new Ontology();
        ontology.setName(ontologyName);
        for(OntologyItemInput item: items) {
            OntologyItem ontologyItem = new OntologyItem();
            ontologyItem.setLabel(item.getLabel());
            ontologyItem.setImportance(item.getImportance());
            ontologyItem.setNotes(item.getNotes());

            if(Stream.of(OntologyItemType.values()).map(Enum::name).toList().contains(item.getType()))
                ontologyItem.setType(OntologyItemType.valueOf(item.getType()));
            else
                ontologyItem.setType(OntologyItemType.UNKNOWN);

            ontology.addItem(ontologyItem);
        }

        ontology = ontologyRepository.save(ontology);

        /*TssStorageClient tssClient = new TssStorageClient(new RestTemplate());
        tssClient.createNewTable(ontologyName, items.stream()
                .collect(Collectors.toMap(OntologyItemInput::getLabel,OntologyItemInput::getType)));*/

        structureService.createTable(ontologyName, items.stream()
                .collect(Collectors.toMap(OntologyItemInput::getLabel,OntologyItemInput::getType)));

        return Converter.toCreateNewOntologyResponse(ontology);
    }

    @Transactional
    public GetOntologyResponse getOntologyInformation(Long ontologyId) {
        Ontology ontology = ontologyRepository.findById(ontologyId).get();

        return Converter.toGetOntologyResponse(ontology);
    }

    public GetOntologyCollectionResponse getOntologies(String name) {
        log.info("getOntologies(): name={}", name);

        List<Ontology> ontologyList;

        if(name == null || name.equals("")) {
            ontologyList = new ArrayList<>();
            ontologyRepository.findAll().iterator().forEachRemaining(ontologyList::add);
        } else {
            ontologyList = ontologyRepository.findAllByName(name);
        }

        log.info("getOntologies(): ontologyList={}", ontologyList);

        GetOntologyCollectionResponse getOntologyCollectionResponse = new GetOntologyCollectionResponse();
        ontologyList.forEach(o -> getOntologyCollectionResponse.addOntology(Converter.toGetOntologyResponse(o)));

        return getOntologyCollectionResponse;
    }

    public void addItemsToOntology(Long ontologyId, List<OntologyItemInput> items) {
        Ontology ontology = ontologyRepository.findById(ontologyId).get();
        for(OntologyItemInput item: items) {
            OntologyItem ontologyItem = new OntologyItem();
            ontologyItem.setLabel(item.getLabel());
            ontologyItem.setImportance(item.getImportance());
            ontologyItem.setNotes(item.getNotes());
            ontologyItem.setType(OntologyItemType.valueOf(item.getType()));
            ontology.addItem(ontologyItem);
        }

        this.ontologyRepository.save(ontology);

        /*TssStorageClient tssClient = new TssStorageClient(new RestTemplate());
        tssClient.addNewColumns(ontology.getName(), items.stream()
                .collect(Collectors.toMap(OntologyItemInput::getLabel,OntologyItemInput::getType)));*/

        structureService.addColumns(ontology.getName(), items.stream()
                .collect(Collectors.toMap(OntologyItemInput::getLabel,OntologyItemInput::getType)));
    }

    public Ontology getOntologyByName(String ontologyName) {
        Ontology ontology = this.ontologyRepository.findAllByName(ontologyName).get(0);

        return ontology;
    }

    static class Converter {

        static CreateNewOntologyResponse toCreateNewOntologyResponse(Ontology ontology) {
            CreateNewOntologyResponse cor = new CreateNewOntologyResponse();
            cor.setOntologyId(ontology.getId());
            cor.setOntologyName(ontology.getName());
            return cor;
        }

        static GetOntologyResponse toGetOntologyResponse(Ontology ontology) {
            GetOntologyResponse gor = new GetOntologyResponse();
            gor.setId(ontology.getId());
            gor.setName(ontology.getName());

            for(OntologyItem oi: ontology.getItems()) {
                OntologyItemOutput oio = new OntologyItemOutput();
                oio.setImportance(oi.getImportance());
                oio.setType(oi.getType().name());
                oio.setLabel(oi.getLabel());
                oio.setNotes(oio.getNotes());

                gor.addItem(oio);
            }

            return gor;
        }

    }

}
