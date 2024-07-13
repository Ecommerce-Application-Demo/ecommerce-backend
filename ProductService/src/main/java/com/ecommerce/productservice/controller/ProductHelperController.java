package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.response.DeliveryTimeResponse;
import com.ecommerce.productservice.entity.Images;
import com.ecommerce.productservice.exception.ErrorResponse;
import com.ecommerce.productservice.exception.ProductException;
import com.ecommerce.productservice.service.declaration.HelperService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/helper")
@Tag(name = "3. Product Helper Controller",description = "Helper APIs related to image & pincode")
public class ProductHelperController {

    @Autowired
    HelperService helperService;
    @Autowired
    Environment environment;

    @Hidden
    @Operation(summary = "Returns modified image URLs with new height,width & quality")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modified Image URLs",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Images.class)) })
    })
    @GetMapping("/image-resize")
    public ResponseEntity<Map<String, String>> imageSize(@RequestBody Map<String, String> images,
                                                         @RequestParam int newHeight,
                                                         @RequestParam int newQuality,
                                                         @RequestParam int newWidth) {

        return new ResponseEntity<>(helperService.imageResizer(images, newHeight, newQuality, newWidth), HttpStatus.OK);
    }

    @Operation(summary = "Returns the day for delivery for specified Pincode,if possible including Warehouse info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Array of Delivery date of the specific Size of that product, if possible " +
                    "and Warehouse info from lowest time to higher in order",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeliveryTimeResponse.class)) }),
            @ApiResponse(responseCode = "406", description = "Uh-oh! We can't deliver there.Try a new address!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/isDeliverable")
    public ResponseEntity<DeliveryTimeResponse> delivery(@RequestParam String pincode,
                                                         @RequestParam String skuId) throws ProductException {
        return new ResponseEntity<>(helperService.getDeliveryAvailability(pincode,skuId), HttpStatus.OK);
    }

    @Operation(summary = "Validate given API secret")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "true",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)) }),
            @ApiResponse(responseCode = "400", description = "API Secret is Invalid",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(value = "/api-secret", consumes = "text/plain" )
    public ResponseEntity<Boolean> apiKeyValidation(@RequestBody String apiSecret) throws ProductException {
       return new ResponseEntity<>(helperService.validateApiKey(apiSecret), HttpStatus.OK);
    }

    @Operation(summary = "Unauthenticated index API")
    @GetMapping("/index")
    public String index(){
        return "From Product Service";
    }

}

