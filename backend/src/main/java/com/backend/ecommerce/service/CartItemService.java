package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.entity.CartItem;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.exception.CartItemException;
import com.backend.ecommerce.exception.UserNotFoundException;

public interface CartItemService {
    CartItem createCartItem(CartItem cartItem);
    CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserNotFoundException;
    CartItem isCartItemExist(Cart cart, Product product, Long userId);
    String removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserNotFoundException;
    CartItem findCartItemById(Long cartItemId) throws CartItemException;
}
