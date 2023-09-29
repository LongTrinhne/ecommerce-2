package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.request.AddItemRequest;

public interface CartService {
    Cart createCart(User user);
    public String addCartItem(Long userId, AddItemRequest itemRequest) throws ProductException;
    public Cart findUserCart(Long userId);
}
