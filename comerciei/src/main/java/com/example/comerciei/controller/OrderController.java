package com.example.comerciei.controller;

import com.example.comerciei.domain.Order;
import com.example.comerciei.domain.User;
import com.example.comerciei.dto.OrderRequestDTO;
import com.example.comerciei.repository.OrderRepository;
import com.example.comerciei.service.OrderService;
import com.example.comerciei.service.StripeService; // Import novo
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final StripeService stripeService; // Injeção nova

    public OrderController(OrderService orderService, OrderRepository orderRepository, StripeService stripeService) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.stripeService = stripeService;
    }

    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody OrderRequestDTO request, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            
            // 1. Gera o pedido no nosso banco de dados
            Order order = orderService.checkout(user, request);
            
            // 2. Chama a Stripe para gerar o link de pagamento
            String paymentUrl = stripeService.createCheckoutSession(order);
            
            // 3. Devolve um JSON amigável com a URL
            Map<String, String> response = new HashMap<>();
            response.put("orderId", order.getId().toString());
            response.put("paymentUrl", paymentUrl);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar pagamento: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Order> orders = orderRepository.findByUser(user);
        return ResponseEntity.ok(orders);
    }
}