package com.example.comerciei.dto;

import com.example.comerciei.domain.UserRole;

public record RegisterDTO(String name, String email, String password, UserRole role) {
}