package com.seproject.inventory.service.impl;

import com.seproject.inventory.entity.Order;
import com.seproject.inventory.entity.Product;
import com.seproject.inventory.entity.User;
import com.seproject.inventory.repository.OrderRepository;
import com.seproject.inventory.repository.ProductRepository;
import com.seproject.inventory.repository.UserRepository;
import com.seproject.inventory.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public Order placeOrder(Long buyerId, Long productId, int quantity) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < quantity)
            throw new RuntimeException("Not enough stock");

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        Order order = new Order();
        order.setBuyer(buyer);
        order.setProduct(product);
        order.setQuantity(quantity);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByBuyer(Long buyerId) {
        return orderRepository.findAll()
                .stream()
                .filter(o -> o.getBuyer().getId().equals(buyerId))
                .toList();
    }
}
