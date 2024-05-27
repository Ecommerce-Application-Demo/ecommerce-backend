package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.ReviewRating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRatingRepo extends CrudRepository<ReviewRating, String> {

    @Query(value = "SELECT COALESCE(AVG(rating),0.0) from product.review_rating where product_id= ?1",nativeQuery = true)
    Float findAvgRating(String productId);

    @Query(value = "SELECT COUNT(*) FROM product.review_rating WHERE product_id=?1", nativeQuery = true)
    Long findCountByProductId(String productId);

    List<ReviewRating> findAllByProductId(String productId);
}
