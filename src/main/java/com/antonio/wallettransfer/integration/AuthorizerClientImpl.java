package com.antonio.wallettransfer.integration;

import com.antonio.wallettransfer.integration.dto.AuthorizerResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthorizerClientImpl implements AuthorizerClient {

    private static final String AUTH_URL = "https://util.devi.tools/api/v2/authorize";

    private final RestTemplate restTemplate;
    private final boolean enabled;

    public AuthorizerClientImpl(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${authorizer.enabled:true}") boolean enabled) {
        this.restTemplate = restTemplateBuilder.build();
        this.enabled = enabled;
    }

    @Override
    public boolean authorize() {
        if (!enabled) {
            return true;
        }

        AuthorizerResponse response = restTemplate.getForObject(AUTH_URL, AuthorizerResponse.class);

        return response != null && response.isAuthorized();
    }
}