package it.uniroma3.copywritergpt.proxy.llm;

import it.uniroma3.copywritergpt.proxy.LlmProxy;
import it.uniroma3.di.common.api.client.chatgpt.ChatGPTClient;
import it.uniroma3.di.common.api.dto.chatgpt.ChatGptRequest;
import it.uniroma3.di.common.api.dto.chatgpt.ChatGptResponse;
import it.uniroma3.di.common.utils.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class ChatGptProxyImpl implements LlmProxy {

    @Override
    public String getResponse(String task, String prompt) {
        log.info("getResponse(): task={}, prompt={}", task, prompt);

        ChatGPTClient chatGPTClient = new ChatGPTClient(new RestTemplate());
        String response = chatGPTClient.getResponse(task, prompt);

        log.info("getResponse(): response={}", response);

        return response;
    }
}
