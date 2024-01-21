package it.uniroma3.copywritergpt.controller.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AddOntologyItemsForm {

    private List<NewOntologyItem> items;

    public AddOntologyItemsForm() {
        this.items = new ArrayList<>();
    }

}
