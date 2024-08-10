package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.WishlistItems;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WishlistRepository extends ListCrudRepository<WishlistItems, String> {
    List<WishlistItems> findByUserId(String userId);
}
