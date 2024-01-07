package it.uniroma3.copywritergpt.proxy;

import it.uniroma3.copywritergpt.domain.vo.Document;
import it.uniroma3.di.common.api.dto.tss.QueryResponse;
import it.uniroma3.di.common.api.dto.tss.ResultEntryResponse;
import it.uniroma3.di.common.utils.Endpoints;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

// TODO Refactor
public class TssProxy {

    private final RestTemplate restTemplate;

    public TssProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public QueryResponse getTopResults(Map<String, String> query, String collectionName, int n) {
        query.put("n", String.valueOf(n));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = Endpoints.TSS_ENDPOINT + "/" + collectionName + "/query";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, String> entry : query.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        url = builder.build().toUriString();

        ResponseEntity<QueryResponse> responseEntity = restTemplate.getForEntity(url, QueryResponse.class);
        return responseEntity.getBody();
    }

    public List<Document> getDocumentsById(String collectionName, Collection<String> documentIds) {
        Map<String,String> solrQuery = new HashMap<>();
        solrQuery.put("id", this.buildQueryById(documentIds));

        List<Document> documents = new ArrayList<>();

        QueryResponse qr = this.getTopResults(solrQuery, collectionName, documentIds.size());
        for(ResultEntryResponse doc: qr.getDocuments()) {
            Document document = new Document();
            document.setFields(new HashMap<>(doc.getColumnName2Content()));
            documents.add(document);
        }

        return documents;
    }

    private String buildQueryById(Collection<String> documentIds) {
        return "(" + String.join(" OR ", documentIds) + ")";
    }


}
