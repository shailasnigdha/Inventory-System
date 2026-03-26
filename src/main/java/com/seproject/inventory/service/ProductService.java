package com.seproject.inventory.service;

import com.seproject.inventory.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product, Long sellerId);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    List<Product> getAllProducts();
    Product getProductById(Long id);
}
