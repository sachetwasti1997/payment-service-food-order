package com.sachet.paymentservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.paymentservice.error.OrderNotFound;
import com.sachet.paymentservice.models.Order;
import com.sachet.paymentservice.models.OrderCancelledEvent;
import com.sachet.paymentservice.models.OrderStatus;
import com.sachet.paymentservice.repositories.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderCancelledConsumer implements AcknowledgingMessageListener<String, String> {
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public OrderCancelledConsumer(OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @KafkaListener(
            topics = "${spring.kafka.cancelorder}",
            groupId = "${spring.kafka.ordercancelledconsumer.group-id}",
            containerFactory = "kafkaOrderCancelledListenerContainerFactory"
    )
    public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("ORDER CANCELLED: {}", consumerRecord.value());
        try {
            var order = objectMapper.readValue(consumerRecord.value(), OrderCancelledEvent.class);
            var orderSaved = orderRepository.findById(order.getOrderId());
            if (orderSaved.isEmpty()) {
                throw new OrderNotFound("Order with "+order.getOrderId()+" not found!",
                        HttpStatus.NOT_FOUND);
            }
            var order1 = orderSaved.get();
            order1.setStatus(OrderStatus.CANCELLED.name());
            orderRepository.save(order1);
            assert acknowledgment != null;
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
