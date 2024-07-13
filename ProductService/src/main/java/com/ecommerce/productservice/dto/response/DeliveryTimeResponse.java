package com.ecommerce.productservice.dto.response;

import java.util.List;

public record DeliveryTimeResponse(boolean isDeliverable, List<DeliveryTimeDetails> deliveryTimeDetails) {}
