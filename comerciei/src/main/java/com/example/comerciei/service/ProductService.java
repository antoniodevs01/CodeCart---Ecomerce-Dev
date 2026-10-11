package com.example.comerciei.service;

import com.example.comerciei.domain.Product;
import com.example.comerciei.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service // Diz ao Spring que esta classe contém a lógica de negócio
public class ProductService {

    // A injeção de dependência: o Service "chama" o Repository
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Regra 1: Listar todos os produtos disponíveis
    public Page<Product> findAllProducts(Pageable pageable) {
    return productRepository.findAll(pageable);
    }

    // Regra 2: Buscar um produto específico
    public Product findProductById(UUID id) {
        // Se não encontrar, "lança" um erro em vez de quebrar o sistema
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));
    }

    // Regra 3: Salvar um novo produto com validação
    public Product saveProduct(Product product) {
        // Exemplo prático de Regra de Negócio: não permitir produtos com preço zerado ou negativo
        if (product.getPrice().doubleValue() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }
        
        return productRepository.save(product);
    }

    // Regra 4: Apagar um produto pelo ID
    public void deleteProduct(UUID id) {
        // O método findProductById já verifica se existe e lança o erro caso não encontre
        Product product = findProductById(id);
        productRepository.delete(product);
    }

    public List<Product> findProductsByCategory(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}