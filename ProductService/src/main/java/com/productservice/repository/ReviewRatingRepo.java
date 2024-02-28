package com.productservice.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.ReviewRating;

public interface ReviewRatingRepo extends CrudRepository<ReviewRating, UUID> {

}
