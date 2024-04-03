package it.uniroma3.copywritergpt.engine;

import it.uniroma3.copywritergpt.domain.vo.Document;
import it.uniroma3.copywritergpt.enums.ArticleType;
import org.aspectj.lang.reflect.DeclareErrorOrWarning;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TemplateFillerTest {

    @Test
    void fillTemplateSingle() {
        Document document = new Document();
        document.getFields().put("Description","desc");
        document.getFields().put("Seller","seller");
        List<Document> docs = new ArrayList<>();
        docs.add(document);

        String template = "Write an article where description=%Description, seller=%Seller";
        TemplateFiller templateFiller = new TemplateFiller();
        String result = templateFiller.fillTemplate(template, docs, ArticleType.SINGLE_DOC);

        assertEquals("Write an article where description=desc, seller=seller", result);
    }

    @Test
    void fillTemplateMultiple() {
        Document document = new Document();
        document.getFields().put("Description","desc");
        document.getFields().put("Seller","seller");
        List<Document> docs = new ArrayList<>();
        docs.add(document);
        Document document2 = new Document();
        document2.getFields().put("Description","desc2");
        document2.getFields().put("Seller","seller2");
        docs.add(document2);

        //String template = "Write an article where %%[Description, Seller]";
        String template = "Write an article where %%[Description,Price,Rating,Color,Seller,Product_Name,Product_Category]";
        TemplateFiller templateFiller = new TemplateFiller();
        String result = templateFiller.fillTemplate(template, docs, ArticleType.MULTIPLE_DOCS);

        assertEquals("Write an article where Description: desc\nSeller: seller\n\nDescription: desc2\nSeller: seller2\n\n", result);
    }

}