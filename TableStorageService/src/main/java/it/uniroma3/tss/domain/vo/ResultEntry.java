package it.uniroma3.tss.domain.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultEntry {

    private Map<String, String> columnName2Content;

    public ResultEntry() {
        this.columnName2Content = new HashMap<>();
    }

    public void addEntry(String columnName, String content) {
        this.columnName2Content.put(columnName, content);
    }

}
