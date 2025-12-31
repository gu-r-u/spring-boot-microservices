package com.microservice.orderservice.config;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean userExists(Long userId) {
        try {
            restTemplate.getForObject(
                    "http://localhost:8081/users/" + userId,
                    Object.class
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
