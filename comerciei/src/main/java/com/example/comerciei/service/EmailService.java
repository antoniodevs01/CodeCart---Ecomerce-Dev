package com.example.comerciei.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmationEmail(String toEmail, java.util.UUID orderId, Double totalAmount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Pagamento Confirmado - Pedido #" + orderId);
        message.setText("Olá!\n\nO pagamento do seu pedido #" + orderId + 
                      " no valor de R$ " + totalAmount + " foi confirmado com sucesso.\n\n" +
                      "Obrigado por comprar na Comerciei!");
        
        mailSender.send(message);
    }
}