package com.example.comerciei.controller;

import com.example.comerciei.domain.Order;
import com.example.comerciei.domain.User;
import com.example.comerciei.dto.OrderRequestDTO;
import com.example.comerciei.repository.OrderRepository;
import com.example.comerciei.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository; // <-- Injetamos o repositório

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<String> checkout(@RequestBody OrderRequestDTO request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Order order = orderService.checkout(user, request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Pedido gerado com sucesso! ID: " + order.getId() + " | Total: R$ " + order.getTotalAmount());
    }

    // <-- O MÉTODO NOVO QUE O SPRING BOOT ESTAVA A PEDIR
    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Order> orders = orderRepository.findByUser(user);
        
        return ResponseEntity.ok(orders);
    }
}