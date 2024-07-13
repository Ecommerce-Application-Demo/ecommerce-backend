package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductFilters;
import com.ecommerce.productservice.dto.request.ProductFilterReq;
import com.ecommerce.productservice.dto.response.ColourInfo;
import com.ecommerce.productservice.dto.response.ListingPageDetails;
import com.ecommerce.productservice.dto.response.SingleProductResponse;
import com.ecommerce.productservice.dto.response.SizeInfo;
import com.ecommerce.productservice.exception.ErrorResponse;
import com.ecommerce.productservice.exception.ProductException;
import com.ecommerce.productservice.service.declaration.ProductSearchService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(value = "/get/search")
@Tag(name = "1. Product Search Controller", description = "REST APIs for getting search results & filters")
public class ProductSearchController {
    @Autowired
    ProductSearchService productService;

    @Operation(summary = "To get single Product details for product page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product with All required details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SingleProductResponse.class))}),
    })
    @GetMapping("/product/{styleName}/{styleId}")
    public ResponseEntity<SingleProductResponse> getProduct(@PathVariable(value = "styleId") String styleId,
                                                            @PathVariable(value = "styleName") String styleName) {

        return new ResponseEntity<>(productService.getSingleProductDetails(styleId, styleName), HttpStatus.OK);
    }

    @Operation(summary = "To get Product details for Product Listing Page with any Search String")
    @Parameter(name = "pageNumber", description = "Number of the page to return")
    @Parameter(name = "sortBy", description = "Sort results by 'popularity', 'HighToLow' & 'LowToHigh' price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product Listing Page details for Specified Search String with Pagination." +
                    " Example : 'Men red tshirts under 499 '",

                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ListingPageDetails.class))}),
            @ApiResponse(responseCode = "400", description = "Page number & Products per page fields must be greater than 0",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/product/listing/{searchString}")
    public ResponseEntity<ListingPageDetails> getProductListingSearchString(
            @PathVariable(value = "searchString") String searchString,
            @ParameterObject @ModelAttribute ProductFilterReq productFilters,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "6") Integer productsPerPage,
            @RequestParam(required = false, defaultValue = "popularity") String sortBy) throws ProductException {
        if (pageNumber > 0 && productsPerPage > 0)
            return new ResponseEntity<>(productService.getProductListingSearchString(searchString, productFilters, sortBy, pageNumber, productsPerPage), HttpStatus.OK);
        else
            throw new ProductException("INVALID_PAGINATION");
    }

    @Operation(summary = "Get all applicable Filter list for specified Search String")
//    @io.swagger.v3.oas.annotations.parameters.RequestBody(content =
//    @Content(mediaType = "application/json",
//            schema = @Schema(example = """
//                    {"sizes": ["S","L","M"]}""")) )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product applicable filters",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductFilters.class))})
    })
    @GetMapping("/product/filters/{searchString}")
    public ResponseEntity<ProductFilters> getProductFilter(@PathVariable(value = "searchString") String searchString,
                                                           @ParameterObject @ModelAttribute ProductFilterReq productFilterReq) {
        return new ResponseEntity<>(productService.getProductFilters(searchString, productFilterReq), HttpStatus.OK);
    }

    @Operation(summary = "To get similar Products details of a specific product style")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Related Products",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ListingPageDetails.class))}),
            @ApiResponse(responseCode = "400", description = "Page number & Products per page fields must be greater than 0",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/product/related/{styleId}")
    public ResponseEntity<ListingPageDetails> getSimilarProducts(@PathVariable(value = "styleId") String styleId,
                                                                 @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                                                                 @RequestParam(required = false, defaultValue = "6") Integer productsPerPage,
                                                                 @RequestParam(required = false, defaultValue = "popularity") String sortBy) throws ProductException {
        if (pageNumber > 0 && productsPerPage > 0)
            return new ResponseEntity<>(productService.getSimilarProducts(styleId, sortBy, pageNumber, productsPerPage), HttpStatus.OK);
        else
            throw new ProductException("INVALID_PAGINATION");
    }

    @Operation(summary = "To get All Colour Variants of a Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Various colour of product both in & out of stock",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ColourInfo.class)))}),
    })
    @GetMapping("/product/colours")
    public ResponseEntity<Set<ColourInfo>> getProductColours(@RequestParam(required = false) String productId,
                                                             @RequestParam(required = false) String styleId) {
        return new ResponseEntity<>(productService.getColours(productId, styleId), HttpStatus.OK);
    }

    @Operation(summary = "To get All available sizes of a Product's Styles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Various sizes of product both in & out of stock",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SizeInfo.class)))}),
    })
    @GetMapping("style/sizes")
    public ResponseEntity<List<SizeInfo>> getStyleSize(@RequestParam String styleId) {
        return new ResponseEntity<>(productService.getSizes(styleId), HttpStatus.OK);
    }


    //----------------------------------------------------------------------------------------------------

    @Hidden
    @Operation(summary = "To get Product details for Product Listing Page with Parameters")
    @Parameter(name = "pageNumber", description = "Number of the page to return")
    @Parameter(name = "numberOfItem", description = "Number of item of the page to return")
    @Parameter(name = "sortBy", description = "Sort results by 'popularity', 'HighToLow' & 'LowToHigh' price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product Listing page details with applied Filters & Pagination",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ListingPageDetails.class))}),
            @ApiResponse(responseCode = "400", description = "Page number & Products per page fields must be greater than 0",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/product/listing")
    public ResponseEntity<ListingPageDetails> getProductListingParameters(
            @RequestParam(required = false) String masterCategoryName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String subCategoryName,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String colour,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) Integer discountPercentage,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "6") Integer productsPerPage,
            @RequestParam(required = false, defaultValue = "popularity") String sortBy) throws ProductException {
        if (pageNumber > 0 && productsPerPage > 0)
            return new ResponseEntity<>(productService.getProductListingParameters(masterCategoryName, categoryName, subCategoryName, brand,
                    gender, colour, size, discountPercentage, minPrice, maxPrice, sortBy, pageNumber, productsPerPage), HttpStatus.OK);
        else
            throw new ProductException("INVALID_PAGINATION");
    }

}
