package com.sachet.paymentservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.paymentservice.models.Order;
import com.sachet.paymentservice.repositories.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderCreatedConsumer implements AcknowledgingMessageListener<String, String> {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public OrderCreatedConsumer(OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @KafkaListener(
            topics = "${spring.kafka.orderevent}",
            groupId = "${spring.kafka.ordercreatedconsumer.group-id}",
            containerFactory = "kafkaOrderCreatedListenerContainerFactory"
    )
    public void onMessage(ConsumerRecord<String, String> consumerRecord,
                          Acknowledgment acknowledgment)
    {
        log.info("ORDER CREATED: {}", consumerRecord.value());
        try {
            var order = objectMapper.readValue(consumerRecord.value(), Order.class);
            orderRepository.save(order);
            assert acknowledgment != null;
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
