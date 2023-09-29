package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.entity.CartItem;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.CartItemException;
import com.backend.ecommerce.exception.UserNotFoundException;
import com.backend.ecommerce.repository.CartItemRepository;
import com.backend.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService{

    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final CartRepository cartRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, UserService userService, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
    }
    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserNotFoundException {
        CartItem item = findCartItemById(id);
        User user = userService.findUserById(item.getUserId());

        if (user.getUserId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            item.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());
        }
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, Long userId) {
        return cartItemRepository.isCartItemExist(cart, product, userId);
    }

    @Override
    public String removeCartItem(Long userId, Long cartItemId)
            throws CartItemException, UserNotFoundException {

        CartItem cartItem = findCartItemById(cartItemId);
        User userInCartItem = userService.findUserById(cartItem.getUserId());

        User user = userService.findUserById(userId);

        if (userInCartItem.getUserId().equals(user.getUserId())) {
            cartItemRepository.deleteById(cartItemId);
            return "Delete sucessfully.";
        }
        else throw new UserNotFoundException("you can not remove another users item");
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

        if (cartItem.isPresent())
            return cartItem.get();
        else throw new CartItemException("Cart item with id: " + cartItemId + " is not existed!");
    }
}
