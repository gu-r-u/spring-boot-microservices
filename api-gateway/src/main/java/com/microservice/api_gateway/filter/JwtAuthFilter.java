package com.microservice.api_gateway.filter;

import com.microservice.api_gateway.config.SecurityProperties;
import com.microservice.api_gateway.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final SecurityProperties securityProperties;

    private static final List<String> PUBLIC_PATHS =
            List.of("/auth/login", "/auth/refresh","/auth/users");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (isPublic(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Missing Authorization header");
        }

        String token = authHeader.substring(7);

        Claims claims;
        try {
            claims = jwtUtil.validateToken(token);
        } catch (ExpiredJwtException e) {
            return unauthorized(exchange, "Token expired");
        } catch (JwtException e) {
            return unauthorized(exchange, "Invalid token");
        }

        exchange.getAttributes().put("JWT_CLAIMS", claims);

        String userId = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);

        if (!isAuthorized(path, roles)) {
            return forbidden(exchange, "Access denied");
        }

        ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .header("X-User-Id", userId)
                .header("X-Roles", String.join(",", roles))
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private boolean isPublic(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private boolean isAuthorized(String path, List<String> roles) {

        for (var entry : securityProperties.getRoleRules().entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return roles.stream()
                        .anyMatch(role ->
                                securityProperties
                                        .resolveRoles(role)
                                        .containsAll(entry.getValue()));
            }
        }
        return true; // no rule = allowed
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String msg) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> forbidden(ServerWebExchange exchange, String msg) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // after correlation filter
    }
}
