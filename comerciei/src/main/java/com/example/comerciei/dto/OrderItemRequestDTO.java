package com.example.comerciei.dto;

import java.util.UUID;

public record OrderItemRequestDTO(UUID productId, Integer quantity) {
}