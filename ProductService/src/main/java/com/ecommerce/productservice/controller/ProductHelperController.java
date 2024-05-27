package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.response.DeliveryTimeDetails;
import com.ecommerce.productservice.entity.Images;
import com.ecommerce.productservice.exceptionhandler.ProductException;
import com.ecommerce.productservice.repository.PincodeRepo;
import com.ecommerce.productservice.service.declaration.HelperService;
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

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/helper")
@Tag(name = "3. Product Helper Controller",description = "Helper APIs related to image & pincode")
public class ProductHelperController {

    @Autowired
    HelperService helperService;
    @Autowired
    PincodeRepo pincodeRepo;
    @Autowired
    Environment environment;

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
                            schema = @Schema(implementation = DeliveryTimeDetails.class)) }),
            @ApiResponse(responseCode = "404", description = "Product not available at your area",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/isDeliverable")
    public ResponseEntity delivery(@RequestParam String pincode,
                                   @RequestParam String sizeId) {
        List<DeliveryTimeDetails> timeDetailsList = helperService.getDeliveryAvailability(pincode,sizeId);
        if (timeDetailsList.isEmpty()) {
            return new ResponseEntity<>(environment.getProperty("PRODUCT_NOT_AVAILABLE_MESSAGE"), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(timeDetailsList, HttpStatus.OK);
        }
    }

    @Operation(summary = "Validate given API secret")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "true",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)) }),
            @ApiResponse(responseCode = "400", description = "API Secret is Invalid",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/api-secret")
    public ResponseEntity<Boolean> apiKeyValidation(@RequestBody String apiSecret) throws ProductException {
       return new ResponseEntity<>(helperService.validateApiKey(apiSecret), HttpStatus.OK);
    }

    @Operation(summary = "Unauthenticated index API")
    @GetMapping("/index")
    public String index(){
        return "From Product Service";
    }
}

