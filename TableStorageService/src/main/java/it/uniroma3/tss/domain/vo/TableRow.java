package it.uniroma3.tss.domain.vo;

import it.uniroma3.tss.utils.Util;
import lombok.Data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Data
public class TableRow {

    private Map<String, String> fieldName2Content;

    public TableRow() {
        this.fieldName2Content = new HashMap<>();
    }

    public void addCell(String fieldName, String content) {
        if(!fieldName2Content.containsKey(fieldName))
            this.fieldName2Content.put(fieldName,content);
    }

    public void removeCellByFieldName(String fieldName) {
        this.fieldName2Content.remove(fieldName);
    }

    public void deleteKeysNotIn(List<String> fieldNamesToPreserve) {
        Iterator<Map.Entry<String, String>> iterator = fieldName2Content.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (!fieldNamesToPreserve.contains(Util.convertToSnakeCase(entry.getKey()))) {
                iterator.remove();
            }
        }
    }

}
