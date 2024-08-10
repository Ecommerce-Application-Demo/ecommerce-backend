package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.entity.WishlistItems;
import com.ecommerce.orderservice.repository.WishlistRepository;
import com.ecommerce.orderservice.service.declaration.WishlistService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Override
    public WishlistItems addToWishlist(String userId, String styleId) {
        WishlistItems wishlistItems = new WishlistItems();
        wishlistItems.setUserId(userId);
        wishlistItems.setStyleId(styleId);
        return wishlistRepository.save(wishlistItems);
    }

    @Override
    public String removeFromWishlist(String userId, String styleId) {
        return null;
    }

    @Override
    public String getWishlist(String userId) {

        return null;
    }

    @Override
    public String moveToCart(String userId, String styleId) {
        return null;
    }
}
