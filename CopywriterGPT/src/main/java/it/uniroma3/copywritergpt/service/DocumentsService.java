package it.uniroma3.copywritergpt.service;

import it.uniroma3.copywritergpt.proxy.TssProxy;
import it.uniroma3.di.common.api.dto.tss.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class DocumentsService {

    private TssProxy tssProxy;

    public DocumentsService() {
        this.tssProxy = new TssProxy(new RestTemplate());
    }

    public QueryResponse getTopResults(Map<String,String> query, int n) {
        return tssProxy.getTopResults(query,n);
    }

}
