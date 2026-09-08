package com.example.blogging.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final List<Product> products = new ArrayList<>();

    // GET /api/products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(products);
    }

    // GET /api/products/101
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {

        return products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/products/search?name=laptop
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String name) {

        List<Product> result = products.stream()
                .filter(product ->
                        product.name()
                                .toLowerCase()
                                .contains(name.toLowerCase()))
                .toList();

        return ResponseEntity.ok(result);
    }

    // POST /api/products
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product) {

        products.add(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(product);
    }

    // PUT /api/products/101
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product updatedProduct) {

        for (int i = 0; i < products.size(); i++) {

            if (products.get(i).id().equals(id)) {

                Product product = new Product(
                        id,
                        updatedProduct.name(),
                        updatedProduct.price(),
                        updatedProduct.category()
                );

                products.set(i, product);

                return ResponseEntity.ok(product);
            }
        }

        return ResponseEntity.notFound().build();
    }

    // DELETE /api/products/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        boolean removed = products.removeIf(
                product -> product.id().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    // Simple record used as the Product model
    public record Product(
            Long id,
            String name,
            double price,
            String category
    ) {
    }
}

