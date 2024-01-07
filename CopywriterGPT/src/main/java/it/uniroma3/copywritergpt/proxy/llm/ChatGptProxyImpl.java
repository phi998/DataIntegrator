package it.uniroma3.copywritergpt.proxy.llm;

import it.uniroma3.copywritergpt.proxy.LlmProxy;
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

    private RestTemplate restTemplate;

    private static final String CHATGPT_GW_URL = Endpoints.CHATGPT_ENDPOINT + "/chat";

    public ChatGptProxyImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getResponse(String task, String prompt) {
        log.info("getResponse(): task={}, prompt={}", task, prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ChatGptRequest chatGptRequest = new ChatGptRequest();
        chatGptRequest.setTask(task);
        chatGptRequest.setPrompt(prompt);

        ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(CHATGPT_GW_URL, chatGptRequest, ChatGptResponse.class);
        ChatGptResponse chatGptResponse = responseEntity.getBody();

        log.info("getResponse(): chatGptResponse={}", chatGptResponse);

        return chatGptResponse.getContent();
    }
}
