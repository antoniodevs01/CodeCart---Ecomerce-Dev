package com.example.comerciei.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; 

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne // Muitos Produtos para Uma Categoria
    @JoinColumn(name = "category_id") // Nome da coluna da chave estrangeira no banco de dados
        private Category category;

    // Método explícito para a IDE parar de acusar erro no Service
    public BigDecimal getPrice() {
        return this.price;
    }

    public Category getCategory() {
    return category;
    }

    public void setCategory(Category category) {
    this.category = category;
    }
}
