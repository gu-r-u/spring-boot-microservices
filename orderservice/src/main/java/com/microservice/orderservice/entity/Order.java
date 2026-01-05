package com.microservice.orderservice.entity;

import com.microservice.orderservice.common.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String product;
    private Double price;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private int retryCount;
    private LocalDateTime lastRetryAt;
}
