package com.example.comerciei.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                    // Vamos libertar apenas as futuras rotas de login e registo
                    .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/auth/login").permitAll()
                    .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/auth/register").permitAll()
                    // Qualquer outra rota exigirá um Token válido
                    .anyRequest().authenticated()
            )
            .build();
}
}