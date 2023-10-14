package com.backend.ecommerce.service.implementation;

import com.backend.ecommerce.entity.Cart;
import com.backend.ecommerce.entity.CartItem;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.repository.CartRepository;
import com.backend.ecommerce.request.AddItemRequest;
import com.backend.ecommerce.service.CartItemService;
import com.backend.ecommerce.service.CartService;
import com.backend.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final ProductService productService;

    public CartServiceImpl(CartRepository cartRepository, CartItemService cartItemService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    @Override
    public Cart createCart(User user) {

        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest itemRequest) throws ProductException {

        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findProductById(itemRequest.getProductId());
        CartItem cartItem = cartItemService.isCartItemExist(cart, product, userId);

        if (cartItem == null) {
            CartItem tmp = new CartItem();
            tmp.setProduct(product);
            tmp.setCart(cart);
            tmp.setQuantity(itemRequest.getQuantity());

            Long price = itemRequest.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
        }
        return "Item add to cart.";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        long totalPrice = 0;
        long totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getPrice();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
            totalItem += cartItem.getQuantity();
        }
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setDiscount(totalPrice - totalDiscountedPrice);

        return cartRepository.save(cart);
    }
}
