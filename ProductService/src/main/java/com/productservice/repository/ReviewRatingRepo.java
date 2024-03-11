package com.productservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.ReviewRating;

public interface ReviewRatingRepo extends CrudRepository<ReviewRating, UUID> {

    @Query(value = "SELECT AVG(rating) from product.review_rating where product_id= ?1",nativeQuery = true)
    Float findAvgRating(UUID productId);
}
