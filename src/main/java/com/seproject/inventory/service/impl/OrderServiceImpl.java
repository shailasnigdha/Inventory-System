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
import com.seproject.inventory.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public Order placeOrder(OrderDto dto) {
        if (dto.getQuantity() <= 0) {
            throw new BadRequestException("Order quantity must be at least 1");
        }

        User buyer = userRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + dto.getBuyerId()));

        Product product = getProductOrThrow(dto.getProductId());
        updateStock(product, dto.getQuantity());

        Order order = new Order();
        order.setBuyer(buyer);
        order.setProduct(product);
        order.setQuantity(dto.getQuantity());

        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long orderId, OrderDto dto) {
        if (dto.getQuantity() <= 0) {
            throw new BadRequestException("Order quantity must be at least 1");
        }

        Order existingOrder = getOrderById(orderId);

        Product previousProduct = existingOrder.getProduct();
        previousProduct.setQuantity(previousProduct.getQuantity() + existingOrder.getQuantity());
        productRepository.save(previousProduct);

        User buyer = userRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + dto.getBuyerId()));

        Product newProduct = getProductOrThrow(dto.getProductId());
        updateStock(newProduct, dto.getQuantity());

        existingOrder.setBuyer(buyer);
        existingOrder.setProduct(newProduct);
        existingOrder.setQuantity(dto.getQuantity());

        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = getOrderById(orderId);
        Product product = order.getProduct();
        product.setQuantity(product.getQuantity() + order.getQuantity());
        productRepository.save(product);
        orderRepository.delete(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByBuyer(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersBySeller(Long sellerId) {
        return orderRepository.findBySellerIdOrderByCreatedAtDesc(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    private Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    private void updateStock(Product product, int quantityToDeduct) {
        if (product.getQuantity() < quantityToDeduct) {
            throw new BadRequestException("Not enough stock for product id: " + product.getId());
        }
        product.setQuantity(product.getQuantity() - quantityToDeduct);
        productRepository.save(product);
    }
}
