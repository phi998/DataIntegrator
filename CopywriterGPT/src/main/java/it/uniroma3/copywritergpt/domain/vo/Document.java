package it.uniroma3.copywritergpt.domain.vo;

import lombok.Data;

import java.util.Map;

@Data
public class Document {

    private Map<String,String> fields;

}
