package com.microservice.orderservice.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private Long userId;
    private String product;
    private Double price;
}
