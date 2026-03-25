package com.seproject.inventory.service;

import com.seproject.inventory.dto.OrderDto;
import com.seproject.inventory.entity.Order;

public interface OrderService {
    Order placeOrder(OrderDto dto, Long buyerId);
}
