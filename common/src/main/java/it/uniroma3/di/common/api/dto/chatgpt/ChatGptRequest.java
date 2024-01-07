package it.uniroma3.di.common.api.dto.chatgpt;

import lombok.Data;

@Data
public class ChatGptRequest {

    private String task;

    private String prompt;

}
