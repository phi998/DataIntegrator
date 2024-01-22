package it.uniroma3.dim.proxy;

import it.uniroma3.di.common.api.dto.dis.FileUploadedResponse;
import it.uniroma3.di.common.api.dto.dis.UploadFileRequest;
import it.uniroma3.di.common.api.dto.tss.TableStorageField;
import it.uniroma3.di.common.api.dto.tss.TableStorageRequest;
import it.uniroma3.di.common.utils.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class TssStorageClient {

    private final RestTemplate restTemplate;
    private final static String TSS_ENDPOINT_TABLES = Endpoints.TSS_ENDPOINT + "/tables";

    // TODO autowiring
    public TssStorageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void uploadJobResultTable(String tableName, String category, String resultTableContent, Map<String,String> ontology2type) {
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

    private static String convertToSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder snakeCase = new StringBuilder();
        boolean lastCharWasSpace = false;

        for (char currentChar : input.toCharArray()) {
            if (Character.isWhitespace(currentChar)) {
                if (!lastCharWasSpace) {
                    snakeCase.append('_');
                    lastCharWasSpace = true;
                }
            } else if (Character.isUpperCase(currentChar) || Character.isDigit(currentChar)) {
                snakeCase.append('_');
                snakeCase.append(Character.toLowerCase(currentChar));
                lastCharWasSpace = false;
            } else {
                snakeCase.append(currentChar);
                lastCharWasSpace = false;
            }
        }

        return snakeCase.toString();
    }

}
