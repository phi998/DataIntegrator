package it.uniroma3.copywritergpt.engine;

import it.uniroma3.copywritergpt.domain.vo.Document;
import it.uniroma3.copywritergpt.enums.ArticleType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TemplateFiller {

    public String fillTemplate(String promptTemplate, List<Document> documents, ArticleType articleType) {
        log.info("fillTemplate(): documents={}, promptTemplate={}, articleType={}", documents, promptTemplate, articleType);

        String result = null;

        switch (articleType) {
            case SINGLE_DOC -> {
                result = this.fillTemplateWithDocument(promptTemplate, documents.get(0));
            }
            case MULTIPLE_DOCS -> {
                result = this.addDocumentsToBottomOfTemplate(promptTemplate, documents);
            }
            case HYBRID -> {
                result = this.fillTemplateWithDocument(promptTemplate, documents.get(0));
                documents.remove(0);
                result = this.addDocumentsToBottomOfTemplate(result,documents);
            }
        }

        log.info("fillTemplate(): result={}", result);

        return result;
    }

    private String fillTemplateWithDocument(String template, Document document) {
        String result = template;

        Map<String,String> attr2content = document.getFields();

        attr2content.remove("id");
        attr2content.remove("_version_");

        for(Map.Entry<String,String> entry: attr2content.entrySet()) {
            String attr = entry.getKey();
            String content = entry.getValue();

            result = result.replaceAll("%" + attr, content);
        }

        return result;
    }

    private String addDocumentsToBottomOfTemplate(String template, List<Document> documents) {
        StringBuilder resultBuilder = new StringBuilder();

        List<String> attrsToConsider = this.getAttributesForDocsList(template);

        for(Document document: documents) {
            Map<String,String> attr2cont = document.getFields();
            for(String attr: attrsToConsider) {
                if(attr2cont.containsKey(attr)) {
                    resultBuilder.append(attr).append(": ").append(attr2cont.get(attr)).append("\n");
                }
            }
            resultBuilder.append("\n");
        }

        return template.replaceAll("%%.*", resultBuilder.toString());
    }

    private List<String> getAttributesForDocsList(String template) {
        Pattern pattern = Pattern.compile("\\[([^\\]]*)\\]");
        Matcher matcher = pattern.matcher(template);

        List<String> resultList = new ArrayList<>();

        while (matcher.find()) {
            String[] resultArray = matcher.group(1).split(",");

            for (String str : resultArray) {
                resultList.add(str.trim());
            }
        }

        return resultList;
    }

}
