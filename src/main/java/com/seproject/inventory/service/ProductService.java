package com.seproject.inventory.service;

import com.seproject.inventory.dto.ProductDto;
import com.seproject.inventory.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDto productDto);
    Product updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    List<Product> getProductsBySeller(Long sellerId);
}
