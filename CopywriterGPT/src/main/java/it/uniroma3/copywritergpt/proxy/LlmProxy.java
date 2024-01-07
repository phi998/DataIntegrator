package it.uniroma3.copywritergpt.proxy;

import it.uniroma3.di.common.api.dto.chatgpt.ChatGptRequest;
import it.uniroma3.di.common.api.dto.chatgpt.ChatGptResponse;

public interface LlmProxy {

    String getResponse(String task, String prompt);

}
