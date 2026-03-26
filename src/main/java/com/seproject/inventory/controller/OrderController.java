package com.seproject.inventory.controller;

import com.seproject.inventory.entity.Order;
import com.seproject.inventory.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // BUYER only
    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/place")
    public Order placeOrder(@RequestParam Long buyerId,
                            @RequestParam Long productId,
                            @RequestParam int quantity) {
        return orderService.placeOrder(buyerId, productId, quantity);
    }

    // BUYER only
    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/buyer/{buyerId}")
    public List<Order> getBuyerOrders(@PathVariable Long buyerId) {
        return orderService.getOrdersByBuyer(buyerId);
    }
}
