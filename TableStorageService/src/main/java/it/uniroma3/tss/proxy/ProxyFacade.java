package it.uniroma3.tss.proxy;

import it.uniroma3.tss.domain.vo.ResultEntry;
import it.uniroma3.tss.domain.vo.TableField;
import it.uniroma3.tss.domain.vo.TableRow;

import java.util.Collection;
import java.util.Map;

public interface ProxyFacade {

    void loadTableStructure(String collectionName, Collection<TableField> fields);

    void uploadData(Map<Integer, TableRow> table);

    Collection<ResultEntry> retrieveData(Map<String,String> query, int nResults);

}
