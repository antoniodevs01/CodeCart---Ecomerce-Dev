package com.example.comerciei.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // IMPORTANTE

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/auth/login").permitAll()
        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/auth/register").permitAll()
        
        // Cumprindo a regra: Qualquer pessoa (mesmo sem login) pode VER o catálogo
        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products").permitAll()
        
        // Cumprindo a regra: Apenas ADMIN pode CRIAR, ATUALIZAR ou APAGAR produtos
        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/products").hasRole("ADMIN")
        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
        
        // Qualquer outra rota exige utilizador logado
        .anyRequest().authenticated()
)
                // ADICIONAMOS O NOSSO FILTRO AQUI:
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}