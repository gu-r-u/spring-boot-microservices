package com.microservice.orderservice.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    public void validateUser(Long userId) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Correlation-Id", MDC.get("X-Correlation-Id"));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(
                    "http://user-service/users/" + userId,
                    HttpMethod.GET,
                    entity,
                    Void.class
            );
        } catch (HttpClientErrorException.NotFound ex) {
            // Business error
            throw new IllegalArgumentException("Invalid user id");
        }
    }
}


