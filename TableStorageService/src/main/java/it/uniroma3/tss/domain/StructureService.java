package it.uniroma3.tss.domain;

import it.uniroma3.tss.domain.vo.TableField;
import it.uniroma3.tss.domain.vo.enums.FieldType;
import it.uniroma3.tss.proxy.clickhouse.ClickhouseProxyFacade;
import it.uniroma3.tss.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class StructureService {

    @Autowired
    private ClickhouseProxyFacade clickhouseProxyFacade;

    public void createTable(String tableName, Map<String,String> fieldName2typeString) {
        List<TableField> fields = new ArrayList<>();

        for(Map.Entry<String,String> entry: fieldName2typeString.entrySet()) {
            String fieldName = entry.getKey();
            String symbolicName = Util.convertToSnakeCase(fieldName);
            FieldType type = FieldType.fromName(entry.getValue());

            TableField tableField = new TableField();
            tableField.setType(type);
            tableField.setName(fieldName);
            tableField.setSymbolicName(symbolicName);
            fields.add(tableField);
        }

        clickhouseProxyFacade.loadTableStructure(tableName, fields);
    }

    public void addColumns(String tableName, Map<String,String> fieldName2typeString) {
        log.info("addColumns(): tableName={}, fieldName2typeString={}",tableName, fieldName2typeString);

        List<TableField> fields = new ArrayList<>();

        for(Map.Entry<String,String> entry: fieldName2typeString.entrySet()) {
            String fieldName = entry.getKey();
            String symbolicName = Util.convertToSnakeCase(fieldName);
            FieldType type = FieldType.fromName(entry.getValue());

            TableField tableField = new TableField();
            tableField.setType(type);
            tableField.setName(fieldName);
            tableField.setSymbolicName(symbolicName);
            fields.add(tableField);
        }

        clickhouseProxyFacade.addColumns(tableName, fields);
    }

    private boolean existsTable(String collectionName) {
        return clickhouseProxyFacade.existsTable(collectionName);
    }

}
