package com.seproject.inventory.service.impl;

import com.seproject.inventory.dto.ProductDto;
import com.seproject.inventory.entity.Product;
import com.seproject.inventory.entity.User;
import com.seproject.inventory.exception.BadRequestException;
import com.seproject.inventory.exception.ResourceNotFoundException;
import com.seproject.inventory.repository.ProductRepository;
import com.seproject.inventory.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDto dto;

    @BeforeEach
    void setUp() {
        dto = new ProductDto();
        dto.setName("Keyboard");
        dto.setPrice(29.99);
        dto.setQuantity(10);
        dto.setSellerId(1L);
    }

    @Test
    void createProduct_Success() {
        User seller = User.builder().id(1L).username("seller1").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product created = productService.createProduct(dto);

        assertEquals("Keyboard", created.getName());
        assertEquals(10, created.getQuantity());
        assertEquals(seller, created.getSeller());
    }

    @Test
    void createProduct_ThrowsWhenSellerMissing() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(dto));
    }

    @Test
    void createProduct_ThrowsWhenPriceInvalid() {
        dto.setPrice(0);
        assertThrows(BadRequestException.class, () -> productService.createProduct(dto));
    }

    @Test
    void updateProduct_Success() {
        Product existing = new Product(4L, "Mouse", "Wireless mouse", 5, 12.5, User.builder().id(2L).build());
        User seller = User.builder().id(1L).username("seller1").build();

        when(productRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product updated = productService.updateProduct(4L, dto);

        assertEquals("Keyboard", updated.getName());
        assertEquals(29.99, updated.getPrice());
        assertEquals(seller, updated.getSeller());
    }

    @Test
    void deleteProduct_ThrowsWhenMissing() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(99L));
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    void getProductsBySeller_UsesRepositoryQuery() {
        when(productRepository.findBySellerId(1L)).thenReturn(List.of(new Product()));

        List<Product> items = productService.getProductsBySeller(1L);

        assertEquals(1, items.size());
        verify(productRepository).findBySellerId(1L);
    }
}

