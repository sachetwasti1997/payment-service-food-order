package com.sachet.paymentservice.services;

import com.sachet.paymentservice.models.Order;
import com.sachet.paymentservice.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
