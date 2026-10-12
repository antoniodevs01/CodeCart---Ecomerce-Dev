package com.example.comerciei.service;

import com.example.comerciei.domain.Order;
import com.example.comerciei.domain.OrderItem;
import com.example.comerciei.domain.Product;
import com.example.comerciei.domain.User;
import com.example.comerciei.dto.OrderItemRequestDTO;
import com.example.comerciei.dto.OrderRequestDTO;
import com.example.comerciei.repository.OrderRepository;
import com.example.comerciei.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
    }

    // @Transactional garante que, se faltar stock a meio da compra, 
    // tudo é revertido (rollback) e o dinheiro/stock não se perde.
    @Transactional
    public Order checkout(User user, OrderRequestDTO request) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING"); // Aguarda pagamento (Fase 4 - Stripe)
        
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDto : request.items()) {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

            // Valida o stock
            if (product.getStockQuantity() < itemDto.quantity()) {
                throw new RuntimeException("Stock insuficiente para o produto: " + product.getName());
            }

            // Desconta o stock do produto
            product.setStockQuantity(product.getStockQuantity() - itemDto.quantity());
            productRepository.save(product);

            // Cria a "linha do recibo"
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setPrice(product.getPrice()); // Congela o preço atual!

            order.getItems().add(orderItem);

            // Calcula o subtotal (Preço * Quantidade) e soma ao total
            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(itemDto.quantity()));
            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    } // <-- FIM DO MÉTODO CHECKOUT

    @Transactional
    public void markOrderAsPaid(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + orderId));
        
        order.setStatus("PAID"); 
        orderRepository.save(order);
        
        System.out.println("SUCESSO: Pedido " + orderId + " atualizado para PAGO!");

        // Disparo do e-mail de confirmação ao cliente
        try {
            String clientEmail = order.getUser().getEmail();
            Double totalAmount = order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0.0;

            emailService.sendOrderConfirmationEmail(clientEmail, orderId, totalAmount);
            System.out.println("E-mail de confirmação disparado com sucesso para: " + clientEmail);
        } catch (Exception e) {
            System.out.println("Erro ao enviar e-mail de confirmação: " + e.getMessage());
        }
    } // <-- FIM DO MÉTODO MARK ORDER AS PAID

}