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
    public ResponseEntity<Product> getProductById(@PathVariable("id") UUID id) {
        Product product = productService.findProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 caso não encontre
        }
        return ResponseEntity.ok(product);
    }

    // Método POST para cadastrar um novo produto
    // URL: POST http://localhost:8080/api/products
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct); // Retorna Status 201 (Created)
    }

    // Método PUT para atualizar um produto existente
    // URL: PUT http://localhost:8080/api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") UUID id, @RequestBody Product productDetails) {
        Product existingProduct = productService.findProductById(id);
        
        if (existingProduct == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o produto não existir
        }

        // Atualiza os dados do produto encontrado com os dados enviados na requisição
        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setStockQuantity(productDetails.getStockQuantity());
        
        Product updatedProduct = productService.saveProduct(existingProduct);
        return ResponseEntity.ok(updatedProduct); // Retorna Status 200 (OK) com o produto atualizado
    }

    // Método DELETE para remover um produto
    // URL: DELETE http://localhost:8080/api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") UUID id) {
        Product existingProduct = productService.findProductById(id);
        
        if (existingProduct == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o produto não existir
        }

        productService.deleteProduct(id); // Garanta que este método existe no seu ProductService
        return ResponseEntity.noContent().build(); // Retorna Status 204 (No Content)
    }
}