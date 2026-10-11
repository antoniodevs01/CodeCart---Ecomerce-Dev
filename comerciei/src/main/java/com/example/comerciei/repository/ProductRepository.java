package com.example.comerciei.repository;

import com.example.comerciei.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    // O Spring Boot cria a query SQL automaticamente só de ler este nome!
    List<Product> findByCategoryId(UUID categoryId);
}
