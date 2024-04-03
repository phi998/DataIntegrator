package it.uniroma3.tss.proxy.solr;

import it.uniroma3.tss.domain.vo.ResultEntry;
import it.uniroma3.tss.domain.vo.TableField;
import it.uniroma3.tss.domain.vo.TableRow;
import it.uniroma3.tss.proxy.ProxyFacade;
import it.uniroma3.tss.proxy.solr.impl.SolrQueryBuilder;
import it.uniroma3.tss.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CoreAdminParams;

import java.io.IOException;
import java.util.*;

@Slf4j
public class SolrProxyFacade implements ProxyFacade {

    private static final String SOLR_ENDPOINT = "http://solr:8983";

    private final SolrClient solrClient;
    private final String collectionName;

    public SolrProxyFacade() {
        this.collectionName = Constants.CORE_NAME;
        this.solrClient = new HttpSolrClient.Builder(SOLR_ENDPOINT + "/solr").build();
    }

    @Override
    public void loadTableStructure(String collectionName, List<TableField> fields) {

    }

    @Override
    public Map<Integer, TableRow> uploadData(String collectionName, Map<Integer, TableRow> table) {
        log.info("uploadData(), table={}", table);

        try {
            for(Map.Entry<Integer, TableRow> row: table.entrySet()) {
                TableRow r = row.getValue();
                Map<String,String> rowCells = r.getFieldName2Content();

                SolrInputDocument doc = new SolrInputDocument();
                for(Map.Entry<String,String> c: rowCells.entrySet()) {
                    doc.addField(c.getKey(),c.getValue() + " ");
                }
                this.solrClient.add(this.collectionName, doc);
            }

            this.solrClient.commit(this.collectionName);
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }

        return table;
    }

    @Override
    public Collection<ResultEntry> retrieveData(String collectionName, Map<String,List<String>> query, int n) {
        log.info("retrieveData(): query={}, n={}", query, n);

        SolrQueryBuilder sqb = new SolrQueryBuilder();

        for(Map.Entry<String,List<String>> cond: query.entrySet()) {
            sqb.addCondition(cond.getKey(),"(" + String.join(" OR ", cond.getValue()) + ")");
        }

        SolrQuery solrQuery = sqb.build();
        solrQuery.setRows(n);

        QueryResponse response = null;
        try {
            response = solrClient.query(collectionName, solrQuery);
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }

        SolrDocumentList results = response.getResults();

        return Converter.solrDocumentListToMapCollection(results);
    }

    private static class Converter {
        static Collection<ResultEntry> solrDocumentListToMapCollection(SolrDocumentList solrDocumentList) {
            List<ResultEntry> mapCollection = new ArrayList<>();

            for (SolrDocument solrDocument : solrDocumentList) {
                Map<String, String> documentMap = new HashMap<>();

                for (String fieldName : solrDocument.getFieldNames()) {
                    documentMap.put(fieldName, String.valueOf(solrDocument.getFieldValue(fieldName)));
                }

                ResultEntry re = new ResultEntry();
                re.setColumnName2Content(documentMap);

                mapCollection.add(re);
            }

            return mapCollection;
        }
    }
}
