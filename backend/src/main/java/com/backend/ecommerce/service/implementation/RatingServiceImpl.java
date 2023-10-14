package com.backend.ecommerce.service.implementation;

import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.Rating;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.repository.RatingRepository;
import com.backend.ecommerce.request.RatingRequest;
import com.backend.ecommerce.service.ProductService;
import com.backend.ecommerce.service.RatingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final ProductService productService;

    public RatingServiceImpl(RatingRepository ratingRepository, ProductService productService) {
        this.ratingRepository = ratingRepository;
        this.productService = productService;
    }

    @Override
    public Rating createRating(RatingRequest ratingRequest, User user) throws ProductException {
        Product product = productService.findProductById(ratingRequest.getProductId());
        Rating rating = new Rating();

        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(ratingRequest.getRating());
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductRatings(Long productId) {
        return ratingRepository.getRatingByProductId(productId);
    }
}
