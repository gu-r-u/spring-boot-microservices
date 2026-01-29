package com.microservice.authorization.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateUserRequest {
    private String username;
    private String password;
    private List<String> roles;
}
