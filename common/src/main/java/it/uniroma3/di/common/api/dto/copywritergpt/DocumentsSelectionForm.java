package it.uniroma3.di.common.api.dto.copywritergpt;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class DocumentsSelectionForm {

    private Long templateId;

    private String collectionName;

    private List<DocumentResultEntry> documents;

    public DocumentsSelectionForm() {
        this.documents = new ArrayList<>();
    }

    public void addDocument(String documentId, Map<String, String> label2Content) {
        DocumentResultEntry documentResultEntry = new DocumentResultEntry();
        documentResultEntry.setDocumentId(documentId);
        documentResultEntry.setLabel2Content(label2Content);
        this.documents.add(documentResultEntry);
    }

}
