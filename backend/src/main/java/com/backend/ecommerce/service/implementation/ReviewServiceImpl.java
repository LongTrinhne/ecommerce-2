package com.backend.ecommerce.service.implementation;

import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.Review;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.repository.ProductRepository;
import com.backend.ecommerce.repository.ReviewRepository;
import com.backend.ecommerce.request.ReviewRequest;
import com.backend.ecommerce.service.ProductService;
import com.backend.ecommerce.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductService productService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductService productService) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
    }

    @Override
    public Review createReview(ReviewRequest reviewRequest, User user) throws ProductException {
        Product product = productService.findProductById(reviewRequest.getProductId());

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(reviewRequest.getReview());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReview(Long productId) {
        return reviewRepository.getAllProductReviewsByProductId(productId);
    }
}
