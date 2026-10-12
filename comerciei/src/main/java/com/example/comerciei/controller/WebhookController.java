package com.example.comerciei.controller;

import com.example.comerciei.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final OrderService orderService;

    public WebhookController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload) {
        try {
            if (payload.contains("checkout.session.completed")) {
                // Expressão regular para encontrar o order_id nos metadados de forma exata
                Pattern pattern = Pattern.compile("\"order_id\":\\s*\"([^\"]+)\"");
                Matcher matcher = pattern.matcher(payload);

                if (matcher.find()) {
                    String orderIdStr = matcher.group(1);
                    UUID orderId = UUID.fromString(orderIdStr);
                    
                    orderService.markOrderAsPaid(orderId);
                    System.out.println("SUCESSO ABSOLUTO: Pedido " + orderId + " atualizado para PAGO via Webhook!");
                } else {
                    System.out.println("AVISO: Evento recebido, mas order_id não foi encontrado nos metadados.");
                }
            }

            return ResponseEntity.ok("Webhook processado com sucesso");
        } catch (Exception e) {
            System.out.println("Erro ao processar webhook: " + e.getMessage());
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}