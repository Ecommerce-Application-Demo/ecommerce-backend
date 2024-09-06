package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.Constants;
import com.ecommerce.orderservice.entity.WishlistItems;
import com.ecommerce.orderservice.repository.WishlistRepository;
import com.ecommerce.orderservice.service.declaration.WishlistService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    RestClient restClient;


    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public WishlistItems addToWishlist(String styleId) {
        return wishlistRepository.save(new WishlistItems(null,getUserId(), styleId));
    }

    @Override
    public void removeFromWishlist(String styleId) {
        wishlistRepository.removeFromWishlist(getUserId(), styleId);
    }

    @Override
    public List getWishlist() {
        ResponseEntity<List> responseEntity = restClient.post()
                .uri(Constants.PRODUCT_GET_STYLES)
                .body(wishlistRepository.findStyleIdByUserId(getUserId()))
                .retrieve()
                .toEntity(List.class);
            return responseEntity.getBody();
    }

    @Override
    public List<String> getWishlistList(){
        return wishlistRepository.findStyleIdByUserId(getUserId());
    }

    @Override
    public String moveToCart(String styleId) {
        return null;
    }
}
