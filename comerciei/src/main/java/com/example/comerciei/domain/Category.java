package com.example.comerciei.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true) // O nome é obrigatório e não pode haver categorias repetidas
    private String name;

    // Construtor vazio (obrigatório para o Spring/Hibernate)
    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}