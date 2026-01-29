package com.microservice.api_gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {

    private List<String> publicPaths;
    private Map<String, List<String>> roleRules;
    private Map<String, List<String>> roleHierarchy;

    public Set<String> resolveRoles(String role) {
        Set<String> resolved = new HashSet<>();
        resolved.add(role);

        if (roleHierarchy.containsKey(role)) {
            resolved.addAll(roleHierarchy.get(role));
        }
        return resolved;
    }

}
