package com.microservice.userservice.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String userId;
    private String username;
    private String password;
}
