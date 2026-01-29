package com.microservice.authorization.controller;

import com.microservice.authorization.dto.CreateUserRequest;
import com.microservice.authorization.dto.UserResponse;
import com.microservice.authorization.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody CreateUserRequest request) {

        return ResponseEntity.ok(userService.createUser(request));
    }
}
