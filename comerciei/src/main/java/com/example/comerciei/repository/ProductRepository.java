package com.example.comerciei.repository;

import com.example.comerciei.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
