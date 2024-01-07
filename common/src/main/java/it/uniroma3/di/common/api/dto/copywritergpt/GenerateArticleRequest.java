package it.uniroma3.di.common.api.dto.copywritergpt;

import lombok.Data;

import java.util.List;

@Data
public class GenerateArticleRequest {

    private Long templateId;

    private List<String> documentsIds;

    private String articleType;

}
