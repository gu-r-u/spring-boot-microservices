package com.microservice.orderservice.service;

import com.microservice.orderservice.common.OrderStatus;
import com.microservice.orderservice.config.UserClient;
import com.microservice.orderservice.dto.OrderRequest;
import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserClient userClient;
    private final OrderRepository orderRepository;

    @CircuitBreaker(name = "userService", fallbackMethod = "userFallback")
    public Order createOrder(OrderRequest request) {

        userClient.validateUser(request.getUserId());

        // continue order creation
        return saveOrder(request);
    }

    // 🔁 Fallback method
    public Order userFallback(OrderRequest request, Exception ex) {

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProduct(request.getProduct());
        order.setPrice(request.getPrice());

        order.setOrderStatus(OrderStatus.PENDING);
        order.setRetryCount(0);
        order.setLastRetryAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Order saveOrder(OrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProduct(request.getProduct());
        order.setPrice(request.getPrice());
        order.setOrderStatus(OrderStatus.CONFIRMED);

        return orderRepository.save(order);
    }
}

