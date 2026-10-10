package com.example.comerciei.repository;

import com.example.comerciei.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // O Spring cria o SQL de busca automaticamente só por causa do nome do método!
    // Isso será essencial para a nossa etapa de Autenticação/Login.
    Optional<User> findByEmail(String email);
}
