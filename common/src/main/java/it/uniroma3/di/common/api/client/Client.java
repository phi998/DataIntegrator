package it.uniroma3.di.common.api.client;

import org.springframework.web.client.RestTemplate;

public abstract class Client {

    protected RestTemplate restTemplate;

    public Client(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
