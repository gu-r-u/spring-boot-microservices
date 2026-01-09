package com.microservice.api_gateway.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityStartupValidator {

    private final SecurityProperties properties;

    @PostConstruct
    public void validateRoles() {

        Set<String> definedRoles = properties.getRoleHierarchy().keySet();
        Set<String> usedRoles = new HashSet<>();

        properties.getRoleHierarchy().values()
                .forEach(usedRoles::addAll);

        properties.getRoleRules().values()
                .forEach(usedRoles::addAll);

        for (String role : usedRoles) {
            if (!definedRoles.contains(role)) {
                throw new IllegalStateException(
                        "Invalid role detected in security config: " + role
                );
            }
        }
    }
}
