package com.backend.ecommerce.controller;

import com.backend.ecommerce.entity.Order;
import com.backend.ecommerce.exception.OrderException;
import com.backend.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
    private OrderService orderService;

    @Autowired
    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/confirmed/{orderId}")
    public ResponseEntity<Order> confirmedOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token) throws OrderException {

        Order order = orderService.confirmedOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/shipped/{orderId}")
    public ResponseEntity<Order> shippedOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token) throws OrderException {

        Order order = orderService.shippedOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/delivered/{orderId}")
    public ResponseEntity<Order> deliveredOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token) throws OrderException {

        Order order = orderService.deliveredOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/canceled/{orderId}")
    public ResponseEntity<Order> canceledOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token) throws OrderException {

        Order order = orderService.canceledOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> deletedOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token) throws OrderException  {

        orderService.deleteOrder(orderId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

}
