package it.uniroma3.dim.rest;

import it.uniroma3.di.common.api.dto.tss.AlterTableRequest;
import it.uniroma3.di.common.api.dto.tss.CreateTableRequest;
import it.uniroma3.dim.domain.StructureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class StructureController {

    @Autowired
    private StructureService structureService;

    @PostMapping("/structures")
    public void createTable(@RequestBody CreateTableRequest createTableRequest) {
        log.info("createTable(): createTableRequest={}", createTableRequest);

        if(!createTableRequest.getColName2type().isEmpty())
            structureService.createTable(createTableRequest.getCollectionName(), createTableRequest.getColName2type());
        else
            log.info("createTable(): empty fields map, skipping table creation");
    }

    @PutMapping("/structures")
    public void alterTable(@RequestBody AlterTableRequest alterTableRequest) {
        log.info("alterTable(): alterTableRequest={}", alterTableRequest);

        structureService.addColumns(alterTableRequest.getCollectionName(), alterTableRequest.getColName2type());
    }

}
