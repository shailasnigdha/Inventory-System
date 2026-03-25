package com.seproject.inventory.service.impl;

import com.seproject.inventory.dto.OrderDto;
import com.seproject.inventory.entity.Order;
import com.seproject.inventory.entity.Product;
import com.seproject.inventory.entity.User;
import com.seproject.inventory.repository.OrderRepository;
import com.seproject.inventory.repository.ProductRepository;
import com.seproject.inventory.repository.UserRepository;
import com.seproject.inventory.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Order placeOrder(OrderDto dto, Long buyerId) {

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Order order = Order.builder()
                .buyer(buyer)
                .product(product)
                .quantity(dto.getQuantity())
                .build();

        return orderRepository.save(order);
    }
}
