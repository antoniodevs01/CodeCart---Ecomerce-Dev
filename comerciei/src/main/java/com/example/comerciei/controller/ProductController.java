package com.example.comerciei.controller;

import com.example.comerciei.domain.Product;
import com.example.comerciei.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController // Diz ao Spring que esta classe vai responder requisições da web (APIs REST)
@RequestMapping("/api/products") // Define a URL base para este controlador
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Método GET para listar todos os produtos
    // URL: GET http://localhost:8080/api/products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        return ResponseEntity.ok(products); // Retorna Status 200 (OK) com a lista
    }

    // Método GET para buscar um produto específico pelo ID
    // URL: GET http://localhost:8080/api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        Product product = productService.findProductById(id);
        return ResponseEntity.ok(product);
    }

    // Método POST para cadastrar um novo produto
    // URL: POST http://localhost:8080/api/products
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct); // Retorna Status 201 (Created)
    }
}
