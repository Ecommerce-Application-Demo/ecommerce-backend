package com.productservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.ReviewRating;

public interface ReviewRatingRepo extends CrudRepository<ReviewRating, UUID> {

    @Query(value = "SELECT COALESCE(AVG(rating),0.0) from product.review_rating where product_id= ?1",nativeQuery = true)
    Float findAvgRating(UUID productId);

    @Query(value = "SELECT COUNT(*) FROM product.review_rating WHERE product_id=?1", nativeQuery = true)
    Long findCountByProductId(UUID productId);

    List<ReviewRating> findAllByProductId(UUID productId);
}
