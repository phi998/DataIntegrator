package it.uniroma3.di.common.api.client.chatgpt;

import it.uniroma3.di.common.api.client.Client;
import it.uniroma3.di.common.api.dto.chatgpt.ChatGptRequest;
import it.uniroma3.di.common.api.dto.chatgpt.ChatGptResponse;
import it.uniroma3.di.common.utils.Endpoints;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ChatGPTClient extends Client {

    private static final String CHATGPT_GW_URL = Endpoints.CHATGPT_ENDPOINT + "/chat";

    public ChatGPTClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public String getResponse(String task, String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ChatGptRequest chatGptRequest = new ChatGptRequest();
        chatGptRequest.setTask(task);
        chatGptRequest.setPrompt(prompt);

        ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(CHATGPT_GW_URL, chatGptRequest, ChatGptResponse.class);
        ChatGptResponse chatGptResponse = responseEntity.getBody();

        return chatGptResponse.getContent();
    }
}
