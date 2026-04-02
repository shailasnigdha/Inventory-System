package com.seproject.inventory.service.impl;

import com.seproject.inventory.dto.ProductDto;
import com.seproject.inventory.entity.Product;
import com.seproject.inventory.entity.User;
import com.seproject.inventory.exception.BadRequestException;
import com.seproject.inventory.exception.ResourceNotFoundException;
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
    public Product createProduct(ProductDto productDto) {
        validateProductData(productDto);
        User seller = userRepository.findById(productDto.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + productDto.getSellerId()));

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setSeller(seller);

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductDto productDto) {
        validateProductData(productDto);
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        User seller = userRepository.findById(productDto.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + productDto.getSellerId()));

        existing.setName(productDto.getName());
        existing.setDescription(productDto.getDescription());
        existing.setPrice(productDto.getPrice());
        existing.setQuantity(productDto.getQuantity());
        existing.setSeller(seller);

        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

    private void validateProductData(ProductDto productDto) {
        if (productDto.getPrice() <= 0) {
            throw new BadRequestException("Product price must be greater than zero");
        }
        if (productDto.getQuantity() < 0) {
            throw new BadRequestException("Product quantity cannot be negative");
        }
    }
}
