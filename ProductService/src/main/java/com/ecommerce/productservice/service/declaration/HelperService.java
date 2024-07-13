package com.ecommerce.productservice.service.declaration;

import com.ecommerce.productservice.dto.response.DeliveryTimeResponse;
import com.ecommerce.productservice.exception.ProductException;

import java.util.Map;

public interface HelperService {

    Map<String,String> imageResizer(Map<String,String> image,int newHeight, int newQuality, int newWidth);

    DeliveryTimeResponse getDeliveryAvailability(String pincode, String skuId) throws ProductException;

    Boolean validateApiKey(String apiSecret) throws ProductException;
}
