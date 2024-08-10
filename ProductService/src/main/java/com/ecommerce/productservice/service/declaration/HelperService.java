package com.ecommerce.productservice.service.declaration;

import com.ecommerce.productservice.dto.response.DeliveryTimeResponse;
import com.ecommerce.productservice.exception.ProductException;

public interface HelperService {

    DeliveryTimeResponse getDeliveryAvailability(String pincode, String skuId) throws ProductException;

    Boolean validateApiKey(String apiSecret) throws ProductException;
}
