package it.uniroma3.tss.domain;

import it.uniroma3.tss.domain.vo.ResultEntry;
import it.uniroma3.tss.domain.vo.TableField;
import it.uniroma3.tss.proxy.ProxyFacade;
import it.uniroma3.tss.proxy.solr.SolrProxyFacade;
import it.uniroma3.di.common.api.dto.tss.ResultEntryResponse;
import it.uniroma3.tss.utils.CSVUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
@Slf4j
public class StorageService {

    public void storeTable(String tableName, String collectionName, String content, Collection<TableField> fields) {
        log.info("storeTable(): tableName={}, collectionName={}, fields={}", tableName, collectionName, fields);

        ProxyFacade proxyFacade = new SolrProxyFacade();
        proxyFacade.uploadData(CSVUtils.toTableRows(content,fields.stream().map(TableField::getName).toList()));
    }

    public Collection<ResultEntryResponse> query(String collectionName, Map<String,String> attributes2values, int n) {
        ProxyFacade proxyFacade = new SolrProxyFacade();

        return proxyFacade.retrieveData(attributes2values, n).stream().map(Converter::toResultEntryResponse).toList();
    }

    private static class Converter {
        static ResultEntryResponse toResultEntryResponse(ResultEntry re) {
            ResultEntryResponse rer = new ResultEntryResponse();
            rer.setColumnName2Content(re.getColumnName2Content());

            return rer;
        }
    }

}
