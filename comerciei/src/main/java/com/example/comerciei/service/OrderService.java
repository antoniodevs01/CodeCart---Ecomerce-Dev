package com.example.comerciei.service;

import com.example.comerciei.domain.Order;
import com.example.comerciei.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Regra: Inicialização e validação de um novo pedido
    public Order createOrder(Order order) {
        // Garantia de estado: Todo novo pedido nasce obrigatoriamente como "PENDENTE"
        order.setStatus("PENDING");
        
        // Garantia financeira: Impede que um bug gere um pedido com valor negativo
        if (order.getTotalAmount() == null || order.getTotalAmount().doubleValue() < 0) {
            throw new IllegalArgumentException("O valor total do pedido não pode ser vazio ou negativo.");
        }

        return orderRepository.save(order);
    }

    public Order findOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado no banco de dados."));
    }
}
