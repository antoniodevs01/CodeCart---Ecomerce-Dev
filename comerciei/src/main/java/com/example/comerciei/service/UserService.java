package com.example.comerciei.service;

import com.example.comerciei.domain.User;
import com.example.comerciei.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Regra: Impede o cadastro de e-mails duplicados
    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }
        
        // Observação para o futuro: É exatamente nesta linha que aplicaremos 
        // a criptografia da senha (BCrypt) quando configurarmos o Spring Security.
        return userRepository.save(user);
    }

    // Regra: Busca segura por ID
    public User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }
}
