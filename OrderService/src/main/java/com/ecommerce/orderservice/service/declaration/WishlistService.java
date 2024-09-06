package com.ecommerce.orderservice.service.declaration;

import com.ecommerce.orderservice.entity.WishlistItems;

import java.util.List;

public interface WishlistService {

    WishlistItems addToWishlist(String styleId);

    void removeFromWishlist(String styleId);

    List getWishlist();

    List<String> getWishlistList();

    String moveToCart(String styleId);
}
