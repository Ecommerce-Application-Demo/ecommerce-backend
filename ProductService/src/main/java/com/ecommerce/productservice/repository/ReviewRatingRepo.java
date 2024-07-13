package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.ReviewRating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface ReviewRatingRepo extends CrudRepository<ReviewRating, String> {

    @Query(value = "SELECT COALESCE(AVG(rating),0.0) from product.review_rating where style_id= ?1",nativeQuery = true)
    Float findAvgRating(String styleId);

    @Query(value = "SELECT COUNT(*) FROM product.review_rating WHERE style_id=?1", nativeQuery = true)
    Long findCountByStyleId(String styleId);

    @Query(value = "SELECT rating, COUNT(rating) AS ratingCount " +
            "FROM product.review_rating r " +
            "WHERE r.style_id = ?1 " +
            "GROUP BY r.rating",nativeQuery = true)
    List<Map> findRatingCountByStyleId(String styleId);

    List<ReviewRating> findAllByStyleId(String styleId);
}
