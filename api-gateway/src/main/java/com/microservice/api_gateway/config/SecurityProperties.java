package com.microservice.api_gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {

    private List<String> publicPaths;
    private Map<String, List<String>> roleRules;
    private Map<String, List<String>> roleHierarchy;
}
