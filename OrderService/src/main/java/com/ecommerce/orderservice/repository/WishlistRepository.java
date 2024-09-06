package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.WishlistItems;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WishlistRepository extends ListCrudRepository<WishlistItems, String> {
    List findByUserId(String userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM order_service.wishlist_items wi " +
                    "WHERE wi.user_id = ?1 AND wi.style_id = ?2", nativeQuery = true)
    void removeFromWishlist(String userId, String styleId);

    @Query(value = "SELECT wi.style_id FROM order_service.wishlist_items wi WHERE wi.user_id = ?1", nativeQuery = true)
    List<String> findStyleIdByUserId(String userId);
}
