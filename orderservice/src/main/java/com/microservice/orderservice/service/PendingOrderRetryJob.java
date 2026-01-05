package com.microservice.orderservice.service;

import com.microservice.orderservice.common.OrderStatus;
import com.microservice.orderservice.config.UserClient;
import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PendingOrderRetryJob {

    private static final int MAX_RETRY = 3;
    private final OrderRepository orderRepository;
    private final UserClient userClient;

    @Scheduled(fixedDelay = 30000) // every 30 seconds
    public void retryPendingOrders() {

        List<Order> pendingOrders =
                orderRepository.findByOrderStatus(OrderStatus.PENDING);

        for (Order order : pendingOrders) {

            try {
                userClient.validateUser(order.getUserId());

                // ✅ User valid
                order.setOrderStatus(OrderStatus.CONFIRMED);
                orderRepository.save(order);

            } catch (IllegalArgumentException ex) {
                // ❌ Business error (invalid user)
                order.setOrderStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);

            } catch (Exception ex) {
                // ⚠️ Technical failure
                handleRetry(order);
            }
        }
    }

    private void handleRetry(Order order) {

        int retryCount = order.getRetryCount() + 1;
        order.setRetryCount(retryCount);
        order.setLastRetryAt(LocalDateTime.now());

        if (retryCount >= MAX_RETRY) {
            order.setOrderStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);
    }
}
