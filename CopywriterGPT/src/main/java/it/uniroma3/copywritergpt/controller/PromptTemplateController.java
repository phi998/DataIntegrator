package it.uniroma3.copywritergpt.controller;

import it.uniroma3.copywritergpt.domain.PromptTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PromptTemplateController {

    @Autowired
    private PromptTemplateService promptTemplateService;



}
