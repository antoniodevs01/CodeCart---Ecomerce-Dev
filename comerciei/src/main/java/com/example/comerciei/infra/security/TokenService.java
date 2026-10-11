package com.example.comerciei.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.comerciei.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // O Spring vai buscar o valor que colocámos no application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("comerciei-api")
                    .withSubject(user.getEmail()) // Guardamos o email do utilizador dentro do token
                    .withExpiresAt(genExpirationDate()) // Tempo de validade
                    .sign(algorithm); // Assinatura final
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("comerciei-api")
                    .build()
                    .verify(token)
                    .getSubject(); // Devolve o email se o token for válido
        } catch (JWTVerificationException exception) {
            return ""; // Devolve string vazia se for inválido ou expirado
        }
    }

    private Instant genExpirationDate() {
        // Define que o token expira daqui a 2 horas (fuso horário de Brasília -03:00)
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}