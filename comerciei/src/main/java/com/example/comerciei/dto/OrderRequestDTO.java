package com.example.comerciei.dto;

import java.util.List;

public record OrderRequestDTO(List<OrderItemRequestDTO> items) {
}