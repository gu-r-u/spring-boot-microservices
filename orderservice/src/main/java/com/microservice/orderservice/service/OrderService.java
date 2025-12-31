package com.microservice.orderservice.service;

import com.microservice.orderservice.config.UserClient;
import com.microservice.orderservice.dto.OrderRequest;
import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;

    public Order createOrder(OrderRequest request) {

        if (!userClient.userExists(request.getUserId())) {
            throw new RuntimeException("User not found");
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProduct(request.getProduct());
        order.setPrice(request.getPrice());

        return orderRepository.save(order);
    }
}

