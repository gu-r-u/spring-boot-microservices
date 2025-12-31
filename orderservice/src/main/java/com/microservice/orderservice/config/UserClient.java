package com.microservice.orderservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    public boolean userExists(Long userId) {
        try {
            restTemplate.getForObject(
                    "http://user-service/users/" + userId,
                    Object.class
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

