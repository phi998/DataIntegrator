package it.uniroma3.copywritergpt.controller.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class StartJobForm {

    private Collection<String> columnsToDrop;

    public StartJobForm() {
        this.columnsToDrop = new ArrayList<>();
    }

}
