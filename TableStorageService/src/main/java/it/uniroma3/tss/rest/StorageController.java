package it.uniroma3.tss.rest;

import it.uniroma3.di.common.api.dto.tss.TableStorageField;
import it.uniroma3.di.common.api.dto.tss.TableStorageRequest;
import it.uniroma3.tss.domain.StorageService;
import it.uniroma3.tss.domain.vo.TableField;
import it.uniroma3.tss.domain.vo.enums.FieldType;
import it.uniroma3.tss.rest.dto.ResultEntryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;

@RestController
@Slf4j
public class StorageController {
    private final static String N_RESULTS_KEY = "n";

    @Autowired
    private StorageService storageService;

    @PostMapping("/tables")
    public void storeTable(@RequestBody TableStorageRequest tsr) {
        log.info("storeTable(): trs={}", tsr);

        String tableContent = new String(Base64.getDecoder().decode(tsr.getContent()), StandardCharsets.UTF_8);
        Collection<TableField> tableFields = tsr.getFields().stream().map(Converter::toTableField).toList();
        storageService.storeTable(tsr.getTableName(), tsr.getCategory(), tableContent, tableFields);
    }

    @GetMapping("/{collectionName}/query")
    public Collection<ResultEntryResponse> getTopResults(@RequestParam Map<String,String> queryParams, @PathVariable("collectionName") String collectionName) {

        log.info("getTopResults(): collectionName={}, queryParams={}", collectionName, queryParams);

        int nResults = Integer.parseInt(queryParams.get(N_RESULTS_KEY));
        queryParams.remove(N_RESULTS_KEY);

        return storageService.query(collectionName, queryParams, nResults);
    }

    private static class Converter {
        static TableField toTableField(TableStorageField storageField) {
            TableField tf = new TableField();
            tf.setName(storageField.getName());
            tf.setType(FieldType.valueOf(storageField.getType()));
            tf.setSymbolicName(storageField.getSymbolicName());
            return tf;
        }
    }


}
