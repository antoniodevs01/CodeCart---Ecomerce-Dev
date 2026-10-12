package com.example.comerciei.service;

import com.example.comerciei.domain.Order;
import com.example.comerciei.domain.OrderItem;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        // Inicializa a biblioteca da Stripe com a sua chave secreta ao ligar o servidor
        Stripe.apiKey = secretKey;
    }

    public String createCheckoutSession(Order order) throws Exception {
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        // Converte os itens do nosso carrinho para o formato que a Stripe exige
        for (OrderItem item : order.getItems()) {
            lineItems.add(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(item.getQuantity().longValue())
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("brl") // Moeda: Real Brasileiro
                            .setUnitAmount(item.getPrice().multiply(new BigDecimal(100)).longValue())
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(item.getProduct().getName())
                                    .build()
                            )
                            .build()
                    )
                    .build()
            );
        }

        // Configura a sessão de pagamento
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:8080/api/orders/success") // Para onde vai se pagar
            .setCancelUrl("http://localhost:8080/api/orders/cancel")   // Para onde vai se desistir
            .addAllLineItem(lineItems)
            .putMetadata("order_id", order.getId().toString()) // Marca invisível para sabermos quem pagou
            .build();

        // Pede à Stripe para criar a página e devolve a URL gerada
        Session session = Session.create(params);
        return session.getUrl();
    }
}