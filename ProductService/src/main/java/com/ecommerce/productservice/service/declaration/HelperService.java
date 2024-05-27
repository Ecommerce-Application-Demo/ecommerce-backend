package com.ecommerce.productservice.service.declaration;

import com.ecommerce.productservice.dto.response.DeliveryTimeDetails;
import com.ecommerce.productservice.exceptionhandler.ProductException;

import java.util.List;
import java.util.Map;

public interface HelperService {

    Map<String,String> imageResizer(Map<String,String> image,int newHeight, int newQuality, int newWidth);

    List<DeliveryTimeDetails> getDeliveryAvailability(String pincode, String sizeId);

    Boolean validateApiKey(String apiSecret) throws ProductException;
}
