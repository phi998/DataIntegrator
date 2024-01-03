package it.uniroma3.di.tss.proxy.solr;

import it.uniroma3.tss.proxy.solr.SolrProxyFacade;
import it.uniroma3.tss.utils.CSVUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class SolrProxyFacadeTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void uploadData() {
        String testData="id,name,age\n" +
                "1,Daniele,28\n" +
                "2,Luca,35\n" +
                "3,Andrea,42\n";

        SolrProxyFacade solrFacade = new SolrProxyFacade("test");
        solrFacade.uploadData(CSVUtils.toTableRows(testData, List.of("id","name","age")));
    }
}