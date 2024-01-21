package it.uniroma3.tss.rest;

import it.uniroma3.di.common.api.dto.tss.QueryResponse;
import it.uniroma3.di.common.api.dto.tss.TableStorageField;
import it.uniroma3.di.common.api.dto.tss.TableStorageRequest;
import it.uniroma3.tss.domain.StorageService;
import it.uniroma3.tss.domain.vo.TableField;
import it.uniroma3.tss.domain.vo.enums.FieldType;
import it.uniroma3.tss.utils.CSVUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static it.uniroma3.di.common.utils.Constants.TSS_N_RESULTS_KEY;

@RestController
@Slf4j
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/tables")
    public void storeTable(@RequestBody TableStorageRequest tsr) {
        log.info("storeTable(): trs={}", tsr);

        String tableContent = new String(Base64.getDecoder().decode(tsr.getContent()), StandardCharsets.UTF_8);

        Collection<TableStorageField> fields = tsr.getFields();
        Collection<TableField> tableFields = new ArrayList<>();
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

        if(queryParams.isEmpty())
            queryParams.put("*","*");

        QueryResponse qr = new QueryResponse();
        qr.setDocuments(storageService.query(queryParams, nResults));

        return qr;
    }

    private static class Converter {
        static TableField toTableField(TableStorageField storageField) {
            TableField tf = new TableField();
            tf.setName(storageField.getName());
            tf.setType(FieldType.valueOf(storageField.getType()));
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
