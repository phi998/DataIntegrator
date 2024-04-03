package it.uniroma3.dim.rest;

import it.uniroma3.di.common.api.dto.tss.QueryResponse;
import it.uniroma3.di.common.api.dto.tss.TableStorageField;
import it.uniroma3.di.common.api.dto.tss.TableStorageRequest;
import it.uniroma3.dim.domain.StorageService;
import it.uniroma3.dim.domain.vo.TableField;
import it.uniroma3.dim.domain.vo.enums.FieldType;
import it.uniroma3.dim.utils.CSVUtils;
import it.uniroma3.dim.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static it.uniroma3.di.common.utils.Constants.TSS_N_RESULTS_KEY;
import static it.uniroma3.di.common.utils.Constants.TSS_TABLE_NAME_KEY;

@RestController
@Slf4j
public class StorageController {

    private final static String ATTR_LIST_SEP = ",";

    @Autowired
    private StorageService storageService;

    @PostMapping("/tables")
    public void storeTable(@RequestBody TableStorageRequest tsr) {
        log.info("storeTable(): trs={}", tsr);

        String tableContent = CSVUtils.considerFirstColByName(tsr.getContent());
        tableContent = CSVUtils.deleteColumnsByName(tableContent, "other","unknown");

        log.info("storeTable(): tableContent={}", tableContent);

        Collection<TableStorageField> fields = tsr.getFields();
        Collection<TableField> tableFields;
        if(fields.isEmpty()) {
            tableFields = CSVUtils.getTableHeaders(tableContent).stream().map(Converter::toStandardTableField).toList();
        } else {
            tableFields = tsr.getFields().stream().map(Converter::toTableField).toList();
        }

        storageService.storeTable(tsr.getTableName(), tsr.getCategory(), tableContent, tableFields);
    }

    @GetMapping("/query")
    public QueryResponse getTopResults(@RequestParam Map<String,String> queryParams) {

        log.info("getTopResults(): queryParams={}", queryParams);

        int nResults = Integer.parseInt(queryParams.get(TSS_N_RESULTS_KEY));
        queryParams.remove(TSS_N_RESULTS_KEY);

        String collectionName = queryParams.get(TSS_TABLE_NAME_KEY);
        queryParams.remove(TSS_TABLE_NAME_KEY);

        QueryResponse qr = new QueryResponse();
        Map<String,List<String>> queryParamsLists = new HashMap<>();
        queryParams.forEach((k,v) -> queryParamsLists.put(k.toLowerCase(), List.of(v.split(ATTR_LIST_SEP))));
        qr.setDocuments(storageService.query(collectionName, queryParamsLists, nResults));

        return qr;
    }

    private static class Converter {
        static TableField toTableField(TableStorageField storageField) {
            TableField tf = new TableField();
            tf.setName(storageField.getName());

            if(Arrays.stream(FieldType.values()).map(FieldType::toString).toList().contains(storageField.getType()))
                tf.setType(FieldType.valueOf(storageField.getType()));
            else
                tf.setType(FieldType.STRING);

            tf.setSymbolicName(storageField.getSymbolicName());
            return tf;
        }

        static TableField toStandardTableField(String fieldName) {
            TableField tf = new TableField();
            tf.setName(fieldName);
            tf.setType(FieldType.TEXT);
            tf.setSymbolicName(fieldName);
            return tf;
        }
    }


}
