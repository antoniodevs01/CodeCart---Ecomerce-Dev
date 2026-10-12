package com.example.comerciei.repository;

import com.example.comerciei.domain.Order;
import com.example.comerciei.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    // O Spring Data JPA cria a query SQL automaticamente só por ler este nome!
    List<Order> findByUser(User user);
    
}