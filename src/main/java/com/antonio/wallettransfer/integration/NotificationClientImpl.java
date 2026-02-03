package com.antonio.wallettransfer.integration;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationClientImpl implements NotificationClient {
    private static final String NOTIFY_URL = "https://util.devi.tools/api/v1/notify";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void notifyUser(Long userId) {
        restTemplate.postForEntity(
                NOTIFY_URL,
                Map.of("userId", userId),
                Void.class);
    }
}
