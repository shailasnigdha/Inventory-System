package com.seproject.inventory.service.impl;

import com.seproject.inventory.dto.OrderDto;
import com.seproject.inventory.entity.Order;
import com.seproject.inventory.entity.Product;
import com.seproject.inventory.entity.User;
import com.seproject.inventory.exception.BadRequestException;
import com.seproject.inventory.exception.ResourceNotFoundException;
import com.seproject.inventory.repository.OrderRepository;
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
class OrderServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderDto dto;

    @BeforeEach
    void setUp() {
        dto = new OrderDto();
        dto.setBuyerId(1L);
        dto.setProductId(2L);
        dto.setQuantity(2);
    }

    @Test
    void placeOrder_Success() {
        User buyer = User.builder().id(1L).build();
        Product product = new Product(2L, "SSD", 10, 90.0, User.builder().id(3L).build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order order = orderService.placeOrder(dto);

        assertEquals(2, order.getQuantity());
        assertEquals(8, product.getQuantity());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void placeOrder_ThrowsWhenStockInsufficient() {
        User buyer = User.builder().id(1L).build();
        Product product = new Product(2L, "SSD", 1, 90.0, User.builder().id(3L).build());

        when(userRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        assertThrows(BadRequestException.class, () -> orderService.placeOrder(dto));
    }

    @Test
    void placeOrder_ThrowsWhenBuyerMissing() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.placeOrder(dto));
    }

    @Test
    void updateOrder_ReleasesOldStockAndAppliesNewStock() {
        User oldBuyer = User.builder().id(1L).build();
        Product oldProduct = new Product(5L, "Mouse", 5, 20.0, User.builder().id(4L).build());
        Order existing = new Order(11L, 2, oldBuyer, oldProduct);

        User newBuyer = User.builder().id(7L).build();
        Product newProduct = new Product(2L, "SSD", 10, 90.0, User.builder().id(3L).build());

        OrderDto updateDto = new OrderDto();
        updateDto.setBuyerId(7L);
        updateDto.setProductId(2L);
        updateDto.setQuantity(3);

        when(orderRepository.findById(11L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(7L)).thenReturn(Optional.of(newBuyer));
        when(productRepository.findById(2L)).thenReturn(Optional.of(newProduct));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order updated = orderService.updateOrder(11L, updateDto);

        assertEquals(7, oldProduct.getQuantity());
        assertEquals(7, newProduct.getQuantity());
        assertEquals(newBuyer, updated.getBuyer());
    }

    @Test
    void deleteOrder_RestoresStock() {
        Product product = new Product(2L, "SSD", 4, 90.0, User.builder().id(3L).build());
        Order order = new Order(11L, 3, User.builder().id(1L).build(), product);

        when(orderRepository.findById(11L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(11L);

        assertEquals(7, product.getQuantity());
        verify(orderRepository).delete(order);
    }

    @Test
    void getOrderById_ThrowsWhenMissing() {
        when(orderRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(404L));
    }

    @Test
    void getOrdersByBuyer_UsesRepositoryQuery() {
        when(orderRepository.findByBuyerId(1L)).thenReturn(List.of(new Order()));

        List<Order> list = orderService.getOrdersByBuyer(1L);

        assertEquals(1, list.size());
        verify(orderRepository).findByBuyerId(1L);
    }
}

