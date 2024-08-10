package com.ecommerce.orderservice.service.declaration;

import com.ecommerce.orderservice.entity.WishlistItems;

public interface WishlistService {

    public WishlistItems addToWishlist(String userId, String styleId);

    public String removeFromWishlist(String userId, String styleId);

    public String getWishlist(String userId);

    public String moveToCart(String userId, String styleId);
}
