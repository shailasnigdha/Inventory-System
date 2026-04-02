package com.seproject.inventory.controller;

import com.seproject.inventory.dto.OrderDto;
import com.seproject.inventory.entity.Order;
import com.seproject.inventory.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping
    public ResponseEntity<Order> placeOrder(@Valid @RequestBody OrderDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(dto));
    }

    @PreAuthorize("hasRole('BUYER')")
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long orderId, @Valid @RequestBody OrderDto dto) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, dto));
    }

    @PreAuthorize("hasAnyRole('BUYER','ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('BUYER','ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOne(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PreAuthorize("hasAnyRole('BUYER','ADMIN')")
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Order>> getBuyerOrders(@PathVariable Long buyerId) {
        return ResponseEntity.ok(orderService.getOrdersByBuyer(buyerId));
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Order>> getSellerOrders(@PathVariable Long sellerId) {
        return ResponseEntity.ok(orderService.getOrdersBySeller(sellerId));
    }

    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        order.setStatus("Delivered");
        orderService.updateOrder(orderId, new OrderDto());
        return ResponseEntity.ok(order);
    }
}
