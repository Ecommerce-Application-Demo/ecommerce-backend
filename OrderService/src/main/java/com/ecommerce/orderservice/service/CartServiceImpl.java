package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.Constants;
import com.ecommerce.orderservice.dto.CartRequest;
import com.ecommerce.orderservice.dto.CartResponse;
import com.ecommerce.orderservice.dto.SkuResponse;
import com.ecommerce.orderservice.entity.Cart;
import com.ecommerce.orderservice.entity.CartItem;
import com.ecommerce.orderservice.repository.CartItemRepository;
import com.ecommerce.orderservice.repository.CartRepository;
import com.ecommerce.orderservice.service.declaration.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    RestClient restClient;

    @Override
    public CartResponse addToCartLoggedInUser(CartRequest cartRequest, String browserSessionId) {

        Cart cart = cartRepository.findByUserId(WishlistServiceImpl.getUserId());
        Cart guestCart = null;
        if (browserSessionId != null)
            guestCart = cartRepository.findByBrowserSessionId(browserSessionId);

        if (cart == null) {                          // if user has no cart
            cart = new Cart();
            cart.setUserId(WishlistServiceImpl.getUserId());
            cart.setCreatedDate(LocalDateTime.now());
            cart = cartRepository.save(cart);

            if (guestCart != null) {                 // if user loggedIn in a browser with guest cart
                Cart finalCart = cart;
                guestCart.getCartItems().forEach(cartItem -> {
                    cartItemRepository.save(new CartItem(null, cartItem.getSkuId(), cartItem.getQuantity(), cartItem.getIsSelected(),finalCart));
                });
                cartRepository.delete(guestCart);
            }

        } else {                                    // if user has a cart

            if (guestCart != null) {                 // Adding guest cart items to user cart
                Cart finalCart = cart;
                List<String> skuIds = cartItemRepository.findSkuIds(cart.getCartId());

                guestCart.getCartItems().forEach(cartItem -> {
                    if (skuIds.contains(cartItem.getSkuId())) {      // if skuId already exists in cart
                        CartItem cartItem1 = cartItemRepository.findByCartIdAndSkuId(finalCart.getCartId(), cartItem.getSkuId());
                        cartItem1.setQuantity(cartItem1.getQuantity() + cartItem.getQuantity());
                        cartItem1.setIsSelected(cartItem.getIsSelected());
                        cartItemRepository.save(cartItem1);
                    } else {                                        // if skuId does not exist in cart
                        cartItemRepository.save(new CartItem(null, cartItem.getSkuId(), cartItem.getQuantity(), cartItem.getIsSelected(), finalCart));
                    }
                });
                cartRepository.delete(guestCart);
            } else                                                  // Adding new item to user cart
                cartItemRepository.save(new CartItem(null, cartRequest.getSkuId(), cartRequest.getQuantity(), cartRequest.getIsSelected(), cart));
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
        return new CartResponse(cart.getCartId(), browserSessionId, cartItems, cartItems.size());
    }

    @Override
    public CartResponse addToCartGuestUser(CartRequest cartRequest, String browserSessionId) {
        Cart cart = cartRepository.findByBrowserSessionId(browserSessionId);
        if (cart == null) {
            cart = new Cart();
            cart.setBrowserSessionId(browserSessionId);
            cart.setCreatedDate(LocalDateTime.now());
            cart = cartRepository.save(cart);
            cartItemRepository.save(new CartItem(null, cartRequest.getSkuId(), cartRequest.getQuantity(), cartRequest.getIsSelected(), cart));
        } else {
            cartItemRepository.save(new CartItem(null, cartRequest.getSkuId(), cartRequest.getQuantity(), cartRequest.getIsSelected(), cart));
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());
        return new CartResponse(cart.getCartId(), browserSessionId, cartItems, cartItems.size());
    }

    @Override
    public CartResponse getCartItems(String browserSessionId, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Cart cart = cartRepository.findByUserId(WishlistServiceImpl.getUserId());
            if (cart != null) {
                return new CartResponse(cart.getCartId(), null, getCartProductDetails(cart.getCartId()), cart.getCartItems().size());
            }
        } else if (request.getHeader(Constants.JWT_HEADER_NAME) == null) {

            Cart cart = cartRepository.findByBrowserSessionId(browserSessionId);
            if (cart != null) {
                return new CartResponse(cart.getCartId(), browserSessionId, getCartProductDetails(cart.getCartId()), cart.getCartItems().size());
            }
        }
        return null;
    }

    public List<SkuResponse> getCartProductDetails(String cartId) {
        List<SkuResponse> response = restClient.post()
                .uri(Constants.PRODUCT_GET_SKU)
                .body(cartItemRepository.findSkuIds(cartId))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<SkuResponse>>() {}).getBody();

        if (response == null)
            return null;
        return response.stream().peek(skuResponse -> {
            CartItem cartItem = cartItemRepository.findByCartIdAndSkuId(cartId, skuResponse.getSkuId());
            skuResponse.setProductCartQuantity(cartItem.getQuantity());
            skuResponse.setIsSelected(cartItem.getIsSelected());
        }).toList();
    }

    @Override
    public CartResponse removeItemFromCart(String browserSessionId, List<String> skuId, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Cart cart = cartRepository.findByUserId(WishlistServiceImpl.getUserId());
            if (cart != null) {
                cartItemRepository.deleteSkuByCartId(cart.getCartId(), skuId.toArray(String[]::new));
                return new CartResponse(cart.getCartId(), null, getCartProductDetails(cart.getCartId()),
                                        cartItemRepository.countItemsByCartId(cart.getCartId()));
            }
        } else if (request.getHeader(Constants.JWT_HEADER_NAME) == null) {

            Cart cart = cartRepository.findByBrowserSessionId(browserSessionId);
            if (cart != null) {
                cartItemRepository.deleteSkuByCartId(cart.getCartId(), skuId.toArray(String[]::new));
                return new CartResponse(cart.getCartId(), browserSessionId, getCartProductDetails(cart.getCartId()),
                        cartItemRepository.countItemsByCartId(cart.getCartId()));
            }
        }
        return null;
    }

    @Override
    public Object updateItemQuantity(String deviceId, CartRequest cartRequest) {
        return null;
    }
}
