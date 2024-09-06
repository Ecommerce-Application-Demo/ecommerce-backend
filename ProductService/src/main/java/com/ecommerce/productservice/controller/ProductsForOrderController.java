package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.response.ProductListingResponse;
import com.ecommerce.productservice.dto.response.SkuResponse;
import com.ecommerce.productservice.exception.ErrorResponse;
import com.ecommerce.productservice.repository.SizeDetailsRepo;
import com.ecommerce.productservice.service.declaration.ProductsForOrderService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@Validated
@SecurityRequirement(name = "apiKey")
@Tag(name = "6. Product Details for Order Service Controller",description = "REST APIs for getting realtime product details for order service APIs. Protected with API Key-Secret")

public class ProductsForOrderController {

    @Autowired
    ProductsForOrderService productService;
    @Autowired
    SizeDetailsRepo sizeDetailsRepo;

    @Operation(summary = "To get List of Styles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Styles",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductListingResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "API Secret is Invalid or API Secret is Invalid",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping("/styles")
    public ResponseEntity<List<ProductListingResponse>> getStyles(@RequestBody List<String> styleIds) {
        return new ResponseEntity<>(productService.getStyles(styleIds), HttpStatus.OK);
    }

    @Operation(summary = "To get List of SKUs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of SKUs",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductListingResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "API Secret is Invalid or API Secret is Invalid",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @PostMapping("/sku")
    public ResponseEntity<List<SkuResponse>> getSkus(@RequestBody List<String> skuIds) {
        return new ResponseEntity<>(productService.getSkus(skuIds), HttpStatus.OK);
    }

}
