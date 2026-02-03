package com.antonio.wallettransfer.integration;

import com.antonio.wallettransfer.integration.dto.AuthorizerResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthorizerClientImpl implements AuthorizerClient {

    private static final String AUTH_URL = "https://util.devi.tools/api/v2/authorize";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean authorize() {
        AuthorizerResponse response = restTemplate.getForObject(AUTH_URL, AuthorizerResponse.class);

        return response != null && response.isAuthorized();
    }
}
