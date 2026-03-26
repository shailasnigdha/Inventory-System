package com.seproject.inventory.controller;

import com.seproject.inventory.entity.Product;
import com.seproject.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // SELLER only
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/create/{sellerId}")
    public Product createProduct(@RequestBody Product product, @PathVariable Long sellerId) {
        return productService.createProduct(product, sellerId);
    }

    // SELLER only
    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted!";
    }

    // Everyone
    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    // Everyone
    @GetMapping("/{id}")
    public Product getOne(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
