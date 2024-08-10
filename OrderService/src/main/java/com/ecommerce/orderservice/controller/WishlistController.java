package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.entity.WishlistItems;
import com.ecommerce.orderservice.service.declaration.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/wishlist")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Wishlist Controller", description = "REST APIs for Adding Products to Wishlist.")
public class WishlistController {

    @Autowired
    private WishlistService wishListService;

    @Operation(summary = "Add product to wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = WishlistItems.class))})
    })
    @PostMapping
    public WishlistItems addToWishlist(String userId, String styleId) {
        return wishListService.addToWishlist(userId, styleId);
    }

    @Operation(summary = "Authenticated index API")
    @GetMapping("/index")
    public String index() {
        return "From Order Service";
    }
}
