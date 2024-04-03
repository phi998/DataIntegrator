package it.uniroma3.dim.domain;

import it.uniroma3.dim.domain.vo.TableField;
import it.uniroma3.dim.domain.vo.enums.FieldType;
import it.uniroma3.dim.proxy.storage.clickhouse.ClickhouseProxyFacade;
import it.uniroma3.dim.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StructureService {

    @Autowired
    private ClickhouseProxyFacade clickhouseProxyFacade;

    public void createTable(String tableName, Map<String,String> fieldName2typeString) {
        List<TableField> fields = new ArrayList<>();

        for(Map.Entry<String,String> entry: fieldName2typeString.entrySet()) {
            String fieldName = entry.getKey().toLowerCase();
            String symbolicName = Utils.convertToSnakeCase(fieldName);
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
            String fieldName = entry.getKey().toLowerCase();
            String symbolicName = Utils.convertToSnakeCase(fieldName);
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
