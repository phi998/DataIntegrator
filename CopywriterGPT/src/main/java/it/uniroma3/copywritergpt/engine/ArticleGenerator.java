package it.uniroma3.copywritergpt.engine;

import it.uniroma3.copywritergpt.proxy.LlmProxy;
import it.uniroma3.copywritergpt.proxy.llm.ChatGptProxyImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ArticleGenerator {

    public String generateArticle(String context, String llmPrompt) {
        log.info("generateArticle(): context={}, llmPrompt={}", context, llmPrompt);

        LlmProxy llmProxy = new ChatGptProxyImpl();
        return llmProxy.getResponse("You are an expert about" + context, llmPrompt);
    }

}
