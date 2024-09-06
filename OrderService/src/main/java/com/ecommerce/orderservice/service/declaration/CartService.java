package com.ecommerce.orderservice.service.declaration;

import com.ecommerce.orderservice.dto.CartRequest;
import com.ecommerce.orderservice.dto.CartResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CartService {
    CartResponse addToCartLoggedInUser(CartRequest cartRequest, String browserSessionId);

    CartResponse addToCartGuestUser(CartRequest cartRequest, String browserSessionId);

    CartResponse getCartItems(String deviceId, HttpServletRequest request);

    CartResponse removeItemFromCart(String deviceId, List<String> skuId, HttpServletRequest request);

    Object updateItemQuantity(String deviceId, CartRequest cartRequest);


}
