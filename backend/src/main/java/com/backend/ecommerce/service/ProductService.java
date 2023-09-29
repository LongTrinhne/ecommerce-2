package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.request.ProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductRequest product);
    String deleteProduct(Long productId) throws ProductException;
    Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> findProductByCategory(String category) throws ProductException;
    Page<Product> getAllProduct(String category, Long minPrice, Long maxPrice,
                                Long minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);

    List<Product> findAllProducts();
}
