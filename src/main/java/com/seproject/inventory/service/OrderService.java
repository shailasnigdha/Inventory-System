package com.seproject.inventory.service;

import com.seproject.inventory.entity.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Long buyerId, Long productId, int quantity);
    List<Order> getOrdersByBuyer(Long buyerId);
}
