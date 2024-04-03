package it.uniroma3.dim.proxy.storage;

import it.uniroma3.dim.domain.vo.ResultEntry;
import it.uniroma3.dim.domain.vo.TableField;
import it.uniroma3.dim.domain.vo.TableRow;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ProxyFacade {

    void loadTableStructure(String collectionName, List<TableField> fields);

    Map<Integer, TableRow> uploadData(String collectionName, Map<Integer, TableRow> table);

    Collection<ResultEntry> retrieveData(String collectionName, Map<String,List<String>> query, int nResults);

}
