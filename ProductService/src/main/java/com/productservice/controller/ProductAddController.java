package com.productservice.controller;

import com.productservice.dto.*;
import com.productservice.dto.request.CategoryRequest;
import com.productservice.dto.request.ProductRequest;
import com.productservice.dto.request.SkuRequest;
import com.productservice.dto.request.SubCategoryRequest;
import com.productservice.entity.ReviewRating;
import com.productservice.entity.Sku;
import com.productservice.service.declaration.ProductAddService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/add")
public class ProductAddController {

    @Autowired
    ProductAddService productService;
    @Autowired
    Environment environment;

    @Operation(summary = "To add Master Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Master Category Added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
             })
    @PostMapping("/master-category")
    public ResponseEntity<String> addMasterCategory(@RequestBody MasterCategoryDto masterCategoryDto){
        productService.addMasterCategory(masterCategoryDto);
        return new ResponseEntity<>(environment.getProperty("MASTER.CATEGORY.ADDED"), HttpStatus.OK);
    }

    @Operation(summary = "To add Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category Added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
    })
    @PostMapping("/category")
    public ResponseEntity<String> addCategory(@RequestBody CategoryRequest categoryRequest){
        productService.addCategory(categoryRequest);
        return new ResponseEntity<>(environment.getProperty("CATEGORY.ADDED"), HttpStatus.OK);
    }

    @Operation(summary = "To add SubCategory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SubCategory Added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
    })
    @PostMapping("/sub-category")
    public ResponseEntity<?> addSubCategory(@RequestBody SubCategoryRequest subCategoryDto){
        productService.addSubCategory(subCategoryDto);
        return new ResponseEntity<>(environment.getProperty("SUB.CATEGORY.ADDED"), HttpStatus.OK);
    }

    @Operation(summary = "To add Brand")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand` Added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
    })
    @PostMapping("/brand")
    public ResponseEntity<?> addBrand(@RequestBody BrandDto brandDto){
        productService.addBrand(brandDto);
        return new ResponseEntity<>(environment.getProperty("BRAND.ADDED"), HttpStatus.OK);
    }

    @Operation(summary = "To add Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product Added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class)) }),
    })
    @PostMapping("/product")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductRequest productDto){
        return new ResponseEntity<>(productService.addProduct(productDto), HttpStatus.OK);
    }

    @Operation(summary = "To add Review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review Added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewRating.class)) }),
    })
    @PostMapping("/review")
    public ResponseEntity<ReviewRating> addReview(@RequestBody ReviewRating reviewRating){
        return new ResponseEntity<>(productService.addReview(reviewRating), HttpStatus.OK);
    }

    @Operation(summary = "To add Product SKUs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product SKU Added",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Sku.class)) }),
    })
    @PostMapping("/product/sku")
    public ResponseEntity<Sku> addProductSku(@RequestBody SkuRequest sku){
        return new ResponseEntity<>(productService.addSku(sku), HttpStatus.OK);
    }
}
