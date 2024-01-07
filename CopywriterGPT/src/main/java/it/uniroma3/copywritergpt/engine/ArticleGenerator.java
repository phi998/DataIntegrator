package it.uniroma3.copywritergpt.engine;

import it.uniroma3.copywritergpt.proxy.LlmProxy;
import it.uniroma3.copywritergpt.proxy.llm.ChatGptProxyImpl;
import org.springframework.web.client.RestTemplate;

public class ArticleGenerator {

    public String generateArticle(String context, String llmPrompt) {
        LlmProxy llmProxy = new ChatGptProxyImpl(new RestTemplate());
        return llmProxy.getResponse("You are an expert about" + context, llmPrompt);
    }

}
