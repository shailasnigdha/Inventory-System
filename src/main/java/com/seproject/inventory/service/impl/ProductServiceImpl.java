package com.seproject.inventory.service.impl;

import com.seproject.inventory.entity.Product;
import com.seproject.inventory.entity.User;
import com.seproject.inventory.repository.ProductRepository;
import com.seproject.inventory.repository.UserRepository;
import com.seproject.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Product createProduct(Product product, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        product.setSeller(seller);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updated) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());
        existing.setQuantity(updated.getQuantity());

        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
