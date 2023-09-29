package com.backend.ecommerce.repository;

import com.backend.ecommerce.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("select r from Rating r where r.product.productId = :productId")
    List<Rating> getRatingByProductId(@Param("productId") Long productId);
}
