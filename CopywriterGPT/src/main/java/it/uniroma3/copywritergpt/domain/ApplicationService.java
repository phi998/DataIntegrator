package it.uniroma3.copywritergpt.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class ApplicationService {

    @Autowired
    private PromptTemplateService promptTemplateService;

    public String generateArticle(Collection<Long> documentIds, Long promptTemplateId) {
        log.info("generateArticle(): documentIds={}, promptTemplateId={}", documentIds, promptTemplateId);

        String promptTemplate = this.promptTemplateService.getPromptTemplateById(promptTemplateId).getContent();

        return "";
    }

}
