package com.sachet.paymentservice.repositories;

import com.sachet.paymentservice.models.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, String> {
}
