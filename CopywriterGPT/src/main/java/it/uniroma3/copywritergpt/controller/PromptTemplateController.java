package it.uniroma3.copywritergpt.controller;

import it.uniroma3.copywritergpt.controller.dto.NewTemplateForm;
import it.uniroma3.copywritergpt.domain.PromptCategoryService;
import it.uniroma3.copywritergpt.domain.PromptTemplateService;
import it.uniroma3.di.common.api.dto.copywritergpt.GetCategoriesCollectionResponse;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class PromptTemplateController {

    @Autowired
    private PromptTemplateService promptTemplateService;

    @Autowired
    private PromptCategoryService promptCategoryService;

    @GetMapping("/templates")
    public String getTemplatesList(
            @PathParam("category") String categoryName,
            Model model) {
        log.info("getTemplatesList()");

        GetCategoriesCollectionResponse categories = promptCategoryService.getAllCategories(categoryName);

        model.addAttribute("categories", categories);
        model.addAttribute("templateForm", new NewTemplateForm());

        return "templates";
    }


    @PostMapping("/templates")
    public String createTemplate(
            @ModelAttribute("templateForm") NewTemplateForm templateForm
            ) {
        log.info("createTemplate(): templateForm={}", templateForm);

        this.promptTemplateService.createPromptTemplate(templateForm.getCategory(), templateForm.getName(), templateForm.getContent());

        return "redirect:/templates";
    }

}
