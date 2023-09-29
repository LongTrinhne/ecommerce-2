package com.backend.ecommerce.controller;

import com.backend.ecommerce.entity.Review;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.exception.UserNotFoundException;
import com.backend.ecommerce.request.ReviewRequest;
import com.backend.ecommerce.service.ReviewService;
import com.backend.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable Long productId)
            throws UserNotFoundException, ProductException {
        List<Review> reviewList = reviewService.getAllReview(productId);
        return new ResponseEntity<>(reviewList, HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(
            @RequestBody ReviewRequest reviewRequest,
            @RequestHeader("Authorization") String token) throws UserNotFoundException, ProductException {

        User user = userService.findUserProfileByJwt(token);
        Review review = reviewService.createReview(reviewRequest, user);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

}
