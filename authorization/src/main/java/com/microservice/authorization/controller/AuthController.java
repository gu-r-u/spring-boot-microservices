package com.microservice.authorization.controller;

import com.microservice.authorization.dto.AuthResponse;
import com.microservice.authorization.dto.LoginRequest;
import com.microservice.authorization.dto.RefreshTokenRequest;
import com.microservice.authorization.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("X-User-Id") String userId) {

        authService.logout(userId);
        return ResponseEntity.ok().build();
    }

}
