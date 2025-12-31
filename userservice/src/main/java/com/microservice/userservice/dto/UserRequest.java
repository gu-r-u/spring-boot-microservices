package com.microservice.userservice.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String email;
}
