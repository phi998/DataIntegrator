package it.uniroma3.copywritergpt.controller.dto;

import lombok.Data;

@Data
public class NewOntologyItem {

    private String label;

    private String type;

    private int importance;

    private String notes;

}
