package com.seproject.inventory.web;

import com.seproject.inventory.entity.Order;
import com.seproject.inventory.entity.Product;
import com.seproject.inventory.entity.User;
import com.seproject.inventory.service.OrderService;
import com.seproject.inventory.service.ProductService;
import com.seproject.inventory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/web/admin")
@RequiredArgsConstructor
public class AdminWebController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/admin-dashboard";
    }
}

// REST API endpoints for admin data access
@RestController
@RequestMapping("/admin/api")
@RequiredArgsConstructor
class AdminApiController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    /**
     * Get all products in the system
     * @return List of all products
     */
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Get all orders in the system
     * @return List of all orders
     */
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * Get all users in the system
     * @return List of all users
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}

