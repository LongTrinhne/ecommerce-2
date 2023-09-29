package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Address;
import com.backend.ecommerce.entity.Order;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.OrderException;

import java.util.List;

public interface OrderService {
    Order createOrder (User user, Address shippingAdress);
    Order findOrderById(Long orderId) throws OrderException;
    List<Order> usersOrderHistory(Long userId);
    Order placedOrder(Long orderId) throws OrderException;
    Order confirmedOrder(Long orderId) throws OrderException;
    Order shippedOrder(Long orderId) throws OrderException;
    Order deliveredOrder(Long orderId) throws OrderException;
    Order canceledOrder(Long orderId) throws OrderException;
    List<Order> getAllOrders();
    String deleteOrder(Long orderId) throws OrderException;
}
