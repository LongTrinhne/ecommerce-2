package com.backend.ecommerce.service;

import com.backend.ecommerce.entity.Rating;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.request.RatingRequest;

import java.util.List;

public interface RatingService {
    Rating createRating(RatingRequest ratingRequest, User user) throws ProductException;
    List<Rating> getProductRatings(Long productId);
}
