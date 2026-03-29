package com.seproject.inventory.service;

import com.seproject.inventory.dto.OrderDto;
import com.seproject.inventory.entity.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(OrderDto dto);
    Order updateOrder(Long orderId, OrderDto dto);
    void deleteOrder(Long orderId);
    Order getOrderById(Long orderId);
    List<Order> getAllOrders();
    List<Order> getOrdersByBuyer(Long buyerId);
}
