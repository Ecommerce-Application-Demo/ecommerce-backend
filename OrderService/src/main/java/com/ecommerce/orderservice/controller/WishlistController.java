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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wishlist")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Wishlist Controller", description = "REST APIs for Adding Products to Wishlist.")
public class WishlistController {

    @Autowired
    private WishlistService wishListService;

    @Operation(summary = "Add product to wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added product to wishlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = WishlistItems.class))})
    })
    @PostMapping("/add")
    public WishlistItems addToWishlist(@RequestParam String styleId) {
        return wishListService.addToWishlist(styleId);
    }

    @Operation(summary = "Remove product from wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Removed product from wishlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = WishlistItems.class))})
    })
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromWishlist(String styleId) {
        wishListService.removeFromWishlist(styleId);
        return new ResponseEntity<>("Removed product from wishlist",HttpStatus.OK);
    }

    @Operation(summary = "Get product details of wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist with product details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArrayList.class))})
    })
    @GetMapping("/details")
    public ResponseEntity<List> getWishlistDetails(){
        return new ResponseEntity<>(wishListService.getWishlist(), HttpStatus.OK);
    }

    @Operation(summary = "Get style Ids of wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist styleIds",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArrayList.class))})
    })
    @GetMapping("/list")
    public ResponseEntity<List<String>> getWishlistList(){
        return new ResponseEntity<>(wishListService.getWishlistList(), HttpStatus.OK);
    }

    @Operation(summary = "Authenticated index API")
    @GetMapping("/index")
    public String index() {
        return "From Order Service";
    }
}
