package com.microservice.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private List<String> roles;
}
