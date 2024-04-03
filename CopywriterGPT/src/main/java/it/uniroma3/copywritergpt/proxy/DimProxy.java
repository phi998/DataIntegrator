package it.uniroma3.copywritergpt.proxy;

import it.uniroma3.copywritergpt.domain.vo.Document;
import it.uniroma3.di.common.api.dto.tss.QueryResponse;
import it.uniroma3.di.common.api.dto.tss.ResultEntryResponse;
import it.uniroma3.di.common.utils.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.uniroma3.di.common.utils.Constants.TSS_N_RESULTS_KEY;
import static it.uniroma3.di.common.utils.Constants.TSS_TABLE_NAME_KEY;

@Slf4j
public class DimProxy {

    private final RestTemplate restTemplate;

    public DimProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public QueryResponse getTopResults(String collectionName, Map<String, List<String>> query, int n) {
        log.info("getTopResults(): collectionName={}, query={}, n={}", collectionName, query, n);

        query.put(TSS_N_RESULTS_KEY, List.of(String.valueOf(n)));
        query.put(TSS_TABLE_NAME_KEY, List.of(collectionName));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = Endpoints.DIM_ENDPOINT + "/query";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, List<String>> entry : query.entrySet()) {
            builder.queryParam(entry.getKey(), String.join(",", entry.getValue()));
        }
        url = builder.build().toUriString();

        log.info("getTopResults(): url={}", url);

        ResponseEntity<QueryResponse> responseEntity = restTemplate.getForEntity(url, QueryResponse.class);

        return responseEntity.getBody();
    }

    public List<Document> getDocumentsById(String collectionName, List<String> documentIds) {
        Map<String,List<String>> query = new HashMap<>();
        query.put("id", documentIds);

        List<Document> documents = new ArrayList<>();

        QueryResponse qr = this.getTopResults(collectionName, query, documentIds.size());
        for(ResultEntryResponse doc: qr.getDocuments()) {
            Document document = new Document();
            document.setFields(new HashMap<>(doc.getColumnName2Content()));
            documents.add(document);
        }

        return documents;
    }

}
