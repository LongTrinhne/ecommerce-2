package com.backend.ecommerce.controller;

import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    ResponseEntity<Page<Product>> findProductByCategory(
            @RequestParam String category,@RequestParam Long minPrice,@RequestParam Long maxPrice,
            @RequestParam Long minDiscount, @RequestParam String sort,@RequestParam String stock,
            @RequestParam Integer pageNumber,@RequestParam Integer pageSize) {

        Page<Product> response = productService.getAllProduct(category, minPrice, maxPrice,
                minDiscount, sort, stock, pageNumber, pageSize);
        System.out.println("Complete products!");

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/id/{productId}")
    public ResponseEntity<Product> findProductById(@PathVariable Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }
}
