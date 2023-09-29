package com.backend.ecommerce.controller;

import com.backend.ecommerce.entity.Address;
import com.backend.ecommerce.entity.Order;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.OrderException;
import com.backend.ecommerce.exception.UserNotFoundException;
import com.backend.ecommerce.service.OrderService;
import com.backend.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistory(
            @RequestHeader("Authorization") String token) throws UserNotFoundException {

        User user = userService.findUserProfileByJwt(token);

        List<Order> orderList = orderService.usersOrderHistory(user.getUserId());

        return new ResponseEntity<>(orderList, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> findOrderById(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token) throws UserNotFoundException, OrderException {

        User user = userService.findUserProfileByJwt(token);
        Order order = orderService.findOrderById(orderId);

        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PostMapping("/")
    public ResponseEntity<Order> createOrder(
            @RequestBody Address shippingAddress,
            @RequestHeader("Authorization") String token) throws UserNotFoundException {

        User user = userService.findUserProfileByJwt(token);
        Order order = orderService.createOrder(user, shippingAddress);

        System.out.println("Order: " + order);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

}
