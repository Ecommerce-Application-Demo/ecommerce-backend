package com.productservice.controller;

import com.productservice.dto.*;
import com.productservice.entity.ReviewRating;
import com.productservice.entity.Sku;
import com.productservice.service.declaration.ProductGetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/get")
public class ProductGetController {

    @Autowired
    ProductGetService productService;
    @Autowired
    Environment environment;

    @Operation(summary = "To get Master Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Master Category",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MasterCategoryDto.class)) }),
    })
    @GetMapping("/master-category")
    public ResponseEntity<MasterCategoryDto> getMasterCategory(){
        return new ResponseEntity<>(productService.getMasterCategory(), HttpStatus.OK);
    }

    @Operation(summary = "To get Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class)) }),
    })
    @GetMapping("/category")
    public ResponseEntity<CategoryDto> getCategory(){
        return new ResponseEntity<>(productService.getCategory(), HttpStatus.OK);
    }

    @Operation(summary = "To get SubCategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SubCategory",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubCategoryDto.class)) }),
    })
    @GetMapping("/sub-category")
    public ResponseEntity<SubCategoryDto> getSubCategory(){
        return new ResponseEntity<>(productService.getSubCategory(), HttpStatus.OK);
    }

    @Operation(summary = "To get Brand")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
    })
    @GetMapping("/brand")
    public ResponseEntity<?> getBrand(){
        return new ResponseEntity<>(productService.getBrand(), HttpStatus.OK);
    }

    @Operation(summary = "To get Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class)) }),
    })
    @GetMapping("/product")
    public ResponseEntity<ProductDto> getProduct(){
        return new ResponseEntity<>(productService.getProduct(), HttpStatus.OK);
    }

    @Operation(summary = "To get Review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review geted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewRating.class)) }),
    })
    @GetMapping("/review/{productid}")
    public ResponseEntity<ReviewRating> getReview(@PathVariable(name = "productid") UUID productId){
        return new ResponseEntity<>(productService.getReview(productId), HttpStatus.OK);
    }

    @Operation(summary = "To get Product SKUs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product SKU",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sku.class)) }),
    })
    @GetMapping("/product/sku/{productid}")
    public ResponseEntity<Sku> getProductSku(@PathVariable(name = "productid") UUID productId){
        return new ResponseEntity<>(productService.getSku(productId), HttpStatus.OK);
    }
}