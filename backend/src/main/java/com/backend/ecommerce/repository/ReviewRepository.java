package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.product.productId = :productId")
    List<Review> getAllProductReviewsByProductId(@Param("productId") Long productId);
}
