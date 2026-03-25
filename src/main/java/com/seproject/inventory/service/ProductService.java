package com.seproject.inventory.service;

import com.seproject.inventory.dto.ProductDto;
import com.seproject.inventory.entity.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(ProductDto dto, Long sellerId);
    List<Product> getAllProducts();
}
