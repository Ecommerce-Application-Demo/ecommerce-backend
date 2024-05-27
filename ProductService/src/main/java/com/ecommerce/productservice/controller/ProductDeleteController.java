package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.service.declaration.ProductDeleteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/delete")
@Tag(name = "5. Product Delete Controller",description = "REST APIs for deleting added product style or sizes")
public class ProductDeleteController {

    @Autowired
    ProductDeleteService productDeleteService;

    @Operation(summary = "To delete Product Styles Sizes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Size deleted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "401", description = "API Secret is Invalid or API Secret is Invalid",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })
    })
    @DeleteMapping("/size")
    public String deleteSize(@RequestParam String sizeId){
        productDeleteService.deleteSize(sizeId);
        return "Size deleted successfully";
    }

    @Operation(summary = "To delete Product Styles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Style deleted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "401", description = "API Secret is Invalid or API Secret is Invalid",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })
    })
    @DeleteMapping("/style")
    public String deleteStyle(@RequestParam String styleId){
        productDeleteService.deleteStyle(styleId);
        return "Style deleted successfully";
    }

    @Operation(summary = "To delete Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "401", description = "API Secret is Invalid or API Secret is Invalid",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) })
    })
    @DeleteMapping("/product")
    public String deleteProduct(@RequestParam String productId){
        productDeleteService.deleteProduct(productId);
        return "Product deleted successfully";
    }
}
