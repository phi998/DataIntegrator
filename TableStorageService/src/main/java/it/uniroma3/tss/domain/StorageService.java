package it.uniroma3.tss.domain;

import it.uniroma3.tss.domain.vo.ResultEntry;
import it.uniroma3.tss.domain.vo.TableField;
import it.uniroma3.tss.domain.vo.TableRow;
import it.uniroma3.tss.proxy.ProxyFacade;
import it.uniroma3.tss.proxy.clickhouse.ClickhouseProxyFacade;
import it.uniroma3.tss.proxy.solr.SolrProxyFacade;
import it.uniroma3.di.common.api.dto.tss.ResultEntryResponse;
import it.uniroma3.tss.utils.CSVUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static it.uniroma3.tss.utils.Constants.CORE_NAME;

@Service
@Slf4j
public class StorageService {

    @Autowired
    private ClickhouseProxyFacade clickhouseProxyFacade;

    @Transactional
    public void storeTable(String tableName, String collectionName, String content, Collection<TableField> fields) {
        log.info("storeTable(): tableName={}, collectionName={}, fields={}", tableName, collectionName, fields);

        Map<Integer, TableRow> rows = CSVUtils.toTableRows(content,CSVUtils.getTableHeaders(content));
        clickhouseProxyFacade.uploadData(collectionName, rows);
    }

    public Collection<ResultEntryResponse> query(String collectionName, Map<String, List<String>> attributes2values, int n) {
        log.info("query(): collectionName={}, attributes2values={}, n={}",collectionName,attributes2values,n);

        return clickhouseProxyFacade.retrieveData(collectionName, attributes2values, n).stream().map(Converter::toResultEntryResponse).toList();
    }

    private static class Converter {
        static ResultEntryResponse toResultEntryResponse(ResultEntry re) {
            ResultEntryResponse rer = new ResultEntryResponse();
            rer.setColumnName2Content(re.getColumnName2Content());

            return rer;
        }
    }

}
