package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.Constants;
import com.ecommerce.orderservice.dto.CartRequest;
import com.ecommerce.orderservice.dto.CartResponse;
import com.ecommerce.orderservice.exception.ErrorResponse;
import com.ecommerce.orderservice.service.declaration.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/cart")
@Tag(name = "Cart Controller", description = "REST APIs for Adding Products to Cart.")
public class CartController {

    @Autowired
    CartService cartService;

    @Operation(summary = "To add product to cart", description = "This API is used to add product to cart for both " +
            "logged in and guest users. If user is logged in, then the cart details will be saved to the user's account. " +
            "If user is not logged in, then the cart details will be saved to the browser session." +
            "If the user is logged in and has a cart, then the guest cart details will be merged with the user's cart." +
            "Browser session id have to be shared in the header for guest users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Cart Details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Jwt Token is Invalid or Expired",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest cartRequest, HttpServletRequest request) {

        String browserSessionId;
        if (request.getHeader(Constants.BROWSER_SESSION_ID) != null) {
            browserSessionId = request.getHeader(Constants.BROWSER_SESSION_ID);
        } else {
            browserSessionId = UUID.randomUUID().toString();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return new ResponseEntity<>(cartService.addToCartLoggedInUser(cartRequest, browserSessionId), HttpStatus.OK);
        } else if (request.getHeader(Constants.JWT_HEADER_NAME) == null) {
            return new ResponseEntity<>(cartService.addToCartGuestUser(cartRequest, browserSessionId), HttpStatus.OK);
        } else {
            return null;
        }
    }

    @Operation(summary = "To get Cart Details", description = "This API is used to get the cart details for both " +
            "logged in and guest users. If user is logged in, then the cart details will be fetched from the user's account. " +
            "If user is not logged in, then the cart details will be fetched from the browser session." +
            "Browser session id have to be shared in the header for guest users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Full Cart Details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartResponse.class))}),
            @ApiResponse(responseCode = "401", description = "API Secret is Invalid or API Secret is Invalid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/get")
    public ResponseEntity<CartResponse> getCart(HttpServletRequest request) {

        String browserSessionId = null;
        if (request.getHeader(Constants.BROWSER_SESSION_ID) != null) {
            browserSessionId = request.getHeader(Constants.BROWSER_SESSION_ID);
        }
         return new ResponseEntity<>(cartService.getCartItems(browserSessionId,request), HttpStatus.OK);
    }

    @Operation(summary = "To remove product from cart", description = "This API is used to remove product from cart for both " +
            "logged in and guest users. Browser session id have to be shared in the header for guest users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Cart Details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartResponse.class))}),
            @ApiResponse(responseCode = "401", description = "API Secret is Invalid or API Secret is Invalid",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("/remove")
    public ResponseEntity<CartResponse> removeFromCart(@RequestBody List<String> skuId, HttpServletRequest request) {

        String browserSessionId = null;
        if (request.getHeader(Constants.BROWSER_SESSION_ID) != null) {
            browserSessionId = request.getHeader(Constants.BROWSER_SESSION_ID);
        }
        return new ResponseEntity<>(cartService.removeItemFromCart(browserSessionId,skuId,request), HttpStatus.OK);
    }



    @Operation(summary = "Unauthenticated index API")
    @GetMapping("/index")
    public String index() {
        return "From Order Service";
    }
}
