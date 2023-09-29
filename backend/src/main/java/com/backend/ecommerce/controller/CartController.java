package com.backend.ecommerce.controller;

import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.exception.UserNotFoundException;
import com.backend.ecommerce.request.AddItemRequest;
import com.backend.ecommerce.service.CartService;
import com.backend.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<Cart> findCartByUserId(
            @RequestHeader("Authorization") String token) throws UserNotFoundException {
        User user = userService.findUserProfileByJwt(token);
        Cart cart = cartService.findUserCart(user.getUserId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<String> addItemToCart(
            @RequestBody AddItemRequest addItemRequest,
            @RequestHeader("Authorization") String token) throws UserNotFoundException, ProductException {

        User user = userService.findUserProfileByJwt(token);
        cartService.addCartItem(user.getUserId(), addItemRequest);
        return new ResponseEntity<>("Add Successfully!", HttpStatus.OK);
    }
}
