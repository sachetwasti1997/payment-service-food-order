package com.sachet.paymentservice.controller;

import com.sachet.paymentservice.models.Order;
import com.sachet.paymentservice.repositories.OrderRepository;
import com.sachet.paymentservice.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @GetMapping("/orders")
    public ResponseEntity<Iterable<Order>> getAllOrders() {
        return ResponseEntity.ok(paymentService.getAllOrders());
    }

}
