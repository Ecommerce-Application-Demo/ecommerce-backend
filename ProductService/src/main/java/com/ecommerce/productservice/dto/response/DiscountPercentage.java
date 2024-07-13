package com.ecommerce.productservice.dto.response;

import java.math.BigDecimal;

public record DiscountPercentage(BigDecimal discountPercentage, String discountPercentageText) {
}
