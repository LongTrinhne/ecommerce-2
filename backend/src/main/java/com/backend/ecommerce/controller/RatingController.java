package com.backend.ecommerce.controller;

import com.backend.ecommerce.entity.Rating;
import com.backend.ecommerce.entity.User;
import com.backend.ecommerce.exception.ProductException;
import com.backend.ecommerce.exception.UserNotFoundException;
import com.backend.ecommerce.request.RatingRequest;
import com.backend.ecommerce.service.RatingService;
import com.backend.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingController {
    private final UserService userService;
    private final RatingService ratingService;

    @Autowired
    public RatingController(UserService userService, RatingService ratingService) {
        this.userService = userService;
        this.ratingService = ratingService;
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductRatings(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String token) throws UserNotFoundException, ProductException {
        User user = userService.findUserProfileByJwt(token);
        List<Rating> ratingList = ratingService.getProductRatings(productId);
        return new ResponseEntity<>(ratingList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Rating> createdRating(
            @RequestBody RatingRequest ratingRequest,
            @RequestHeader("Authorization") String token) throws UserNotFoundException, ProductException {
        User user = userService.findUserProfileByJwt(token);
        Rating rating = ratingService.createRating(ratingRequest, user);
        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }
}
