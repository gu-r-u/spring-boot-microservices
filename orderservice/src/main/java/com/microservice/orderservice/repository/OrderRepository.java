package com.microservice.orderservice.repository;

import com.microservice.orderservice.common.OrderStatus;
import com.microservice.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderStatus(OrderStatus status);
}
