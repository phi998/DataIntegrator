package it.uniroma3.copywritergpt.service;

import it.uniroma3.copywritergpt.proxy.DimProxy;
import it.uniroma3.di.common.api.dto.tss.QueryResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class DocumentsService {

    private DimProxy dimProxy;

    public DocumentsService() {
        this.dimProxy = new DimProxy(new RestTemplate());
    }

    public QueryResponse getTopResults(String collectionName, Map<String, List<String>> query, int n) {
        return dimProxy.getTopResults(collectionName,query,n);
    }

}
