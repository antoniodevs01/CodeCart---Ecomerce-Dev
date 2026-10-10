package com.example.comerciei.repository;

import com.example.comerciei.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
