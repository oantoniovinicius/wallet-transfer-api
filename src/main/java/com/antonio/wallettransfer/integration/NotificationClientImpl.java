package com.antonio.wallettransfer.integration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationClientImpl implements NotificationClient {

    private static final String NOTIFY_URL = "https://util.devi.tools/api/v1/notify";

    private final RestTemplate restTemplate;
    private final boolean enabled;

    public NotificationClientImpl(RestTemplateBuilder restTemplateBuilder,
            @Value("${notification.enabled:true}") boolean enabled) {
        this.restTemplate = restTemplateBuilder.build();
        this.enabled = enabled;
    }

    @Override
    public void notifyUser(Long userId) {
        if (!enabled) {
            return;
        }

        restTemplate.postForEntity(
            NOTIFY_URL,
            Map.of("userId", userId),
            Void.class
    );
}
}
