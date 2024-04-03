package it.uniroma3.dim.proxy;

import it.uniroma3.di.common.api.dto.dis.FileUploadedResponse;
import it.uniroma3.di.common.api.dto.dis.UploadFileRequest;
import it.uniroma3.di.common.api.dto.tss.AlterTableRequest;
import it.uniroma3.di.common.api.dto.tss.CreateTableRequest;
import it.uniroma3.di.common.api.dto.tss.TableStorageField;
import it.uniroma3.di.common.api.dto.tss.TableStorageRequest;
import it.uniroma3.di.common.utils.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static it.uniroma3.dim.utils.Utils.convertToSnakeCase;

@Slf4j
public class TssStorageClient {

    private final RestTemplate restTemplate;
    private final static String TSS_ENDPOINT_TABLES = Endpoints.TSS_ENDPOINT + "/tables";
    private final static String TSS_ENDPOINT_STRUCTURES = Endpoints.TSS_ENDPOINT + "/structures";


    // TODO autowiring
    public TssStorageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void uploadJobResultTable(String tableName, String category, String resultTableContent, Map<String,String> ontology2type) {
        log.info("uploadJobResultTable(): tableName={}, category={}, resultTableContent={}, ontology2type={}",
                tableName, category, resultTableContent, ontology2type);

        TableStorageRequest tsr = new TableStorageRequest();
        tsr.setCategory(category);
        tsr.setContent(resultTableContent);
        tsr.setTableName(tableName);
        tsr.setHasHeader(true);

        for(Map.Entry<String,String> ontologyItem : ontology2type.entrySet()) {
            String ontologyItemName = ontologyItem.getKey();
            String ontologyItemType = ontologyItem.getValue();

            TableStorageField tableStorageField = new TableStorageField();
            tableStorageField.setName(ontologyItemName);
            tableStorageField.setType(ontologyItemType);
            tableStorageField.setSymbolicName(convertToSnakeCase(ontologyItemName));

            tsr.addField(tableStorageField);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TableStorageRequest> requestEntity = new HttpEntity<>(tsr, headers);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(TSS_ENDPOINT_TABLES, requestEntity, Void.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("POST request successful. Response: {}", responseEntity.getBody());
        } else {
            log.error("POST request failed. Response: {}", responseEntity.getStatusCode());
        }
    }

    public void createNewTable(String tableName, Map<String,String> colName2type) {
        log.info("createNewTable(): tableName={}, colName2type={}", tableName, colName2type);

        CreateTableRequest createTableRequest = new CreateTableRequest();
        createTableRequest.setCollectionName(tableName);
        createTableRequest.setColName2type(colName2type);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateTableRequest> requestEntity = new HttpEntity<>(createTableRequest, headers);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(TSS_ENDPOINT_STRUCTURES, requestEntity, Void.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("POST request successful. Response: {}", responseEntity.getBody());
        } else {
            log.error("POST request failed. Response: {}", responseEntity.getStatusCode());
        }
    }

    public void addNewColumns(String tableName, Map<String,String> colName2type) {
        log.info("addNewColumns(): tableName={}, colName2type={}", tableName, colName2type);

        AlterTableRequest alterTableRequest = new AlterTableRequest();
        alterTableRequest.setCollectionName(tableName);
        alterTableRequest.setColName2type(colName2type);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AlterTableRequest> requestEntity = new HttpEntity<>(alterTableRequest, headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(TSS_ENDPOINT_STRUCTURES, HttpMethod.PUT, requestEntity, Void.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("PUT request successful. Response: {}", responseEntity.getBody());
        } else {
            log.error("PUT request failed. Response: {}", responseEntity.getStatusCode());
        }
    }

}
