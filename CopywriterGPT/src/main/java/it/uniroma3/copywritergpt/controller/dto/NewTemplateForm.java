package it.uniroma3.copywritergpt.controller.dto;

import lombok.Data;

@Data
public class NewTemplateForm {

    private String category;

    private String name;

    private String content;

}
