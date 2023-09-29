package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.*;
import com.backend.ecommerce.exception.OrderException;
import com.backend.ecommerce.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final OrderItemService orderItemService;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService,
                            AddressRepository addressRepository, UserRepository userRepository,
                            OrderItemService orderItemService, OrderItemRepository orderItemRepository,
                            ProductService productService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {

        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException("order with id: " + orderId + " not exist!")
        );
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepository.getUsersOrders(userId);
    }
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public String deleteOrder(Long orderId) throws OrderException {
        orderRepository.deleteById(orderId);
        return "Order with id: " + orderId + " has been deleted!";
    }
    @Override
    public Order createOrder(User user, Address shippingAdress) {

        shippingAdress.setUser(user);
        Address address = addressRepository.save(shippingAdress);

        user.getAddresses().add(address);
        userRepository.save(user);

        Cart cart = cartService.findUserCart(user.getUserId());
        List<OrderItem> orderItemsList = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUserId(item.getUserId());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());

            OrderItem createdOrderItem = orderItemRepository.save(orderItem);

            orderItemsList.add(createdOrderItem);
        }

        Order createdOrder = new Order();

        createdOrder.setUser(user);
        createdOrder.setOrderItems(orderItemsList);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(cart.getDiscount());
        createdOrder.setTotalItems(cart.getTotalItem());
        createdOrder.setShippingAddress(address);
        createdOrder.setOrderStatus("PENDING");


        Order savedOrder = orderRepository.save(createdOrder);

        for (OrderItem item : orderItemsList){
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("PLACED");
        return orderRepository.save(order);
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("DELIVERED");
        return orderRepository.save(order);
    }

    @Override
    public Order canceledOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CANCELLED");
        return orderRepository.save(order);
    }
}
