package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Review;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.request.ReviewRequest;

import java.util.List;

public interface ReviewService {
    Review createReview(ReviewRequest reviewRequest, User user) throws ProductException;
    List<Review> getAllReview(Long productId);
}
