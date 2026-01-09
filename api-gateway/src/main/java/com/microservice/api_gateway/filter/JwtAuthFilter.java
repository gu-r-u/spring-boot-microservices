package com.microservice.api_gateway.filter;

import com.microservice.api_gateway.config.SecurityProperties;
import com.microservice.api_gateway.security.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final SecurityProperties security;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return error(exchange, HttpStatus.UNAUTHORIZED, "JWT token missing");
        }

        Claims claims;
        try {
            claims = jwtUtil.validateToken(authHeader.substring(7));
        } catch (Exception ex) {
            return error(exchange, HttpStatus.UNAUTHORIZED, "JWT token invalid or expired");
        }

        String userId = claims.getSubject();
        Set<String> effectiveRoles = expandRoles(claims);

        if (!hasAccess(path, effectiveRoles)) {
            return error(exchange, HttpStatus.FORBIDDEN, "Access denied");
        }

        ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .header("X-User-Id", userId)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    // ================= HELPERS =================

    private boolean isPublicPath(String path) {
        return security.getPublicPaths().stream()
                .anyMatch(path::startsWith);
    }

    private Set<String> expandRoles(Claims claims) {

        List<String> jwtRoles = (List<String>) claims.get("roles");
        Set<String> effectiveRoles = new HashSet<>();

        for (String role : jwtRoles) {
            effectiveRoles.addAll(
                    security.getRoleHierarchy().getOrDefault(role, List.of())
            );
        }
        return effectiveRoles;
    }

    private boolean hasAccess(String path, Set<String> roles) {

        for (Map.Entry<String, List<String>> entry
                : security.getRoleRules().entrySet()) {

            if (path.startsWith(entry.getKey())) {
                return entry.getValue().stream()
                        .anyMatch(roles::contains);
            }
        }
        return true;
    }

    private Mono<Void> error(ServerWebExchange exchange,
                             HttpStatus status,
                             String message) {

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message,
                "correlationId",
                exchange.getRequest().getHeaders().getFirst("X-Correlation-Id")
        );

        try {
            byte[] json = objectMapper.writeValueAsBytes(body);
            DataBuffer buffer = exchange.getResponse()
                    .bufferFactory()
                    .wrap(json);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
