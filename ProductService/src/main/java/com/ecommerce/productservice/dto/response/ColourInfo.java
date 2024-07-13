package com.ecommerce.productservice.dto.response;

public record ColourInfo(String styleId,String styleName,String colour,String hexCode, String defaultImage,
                         boolean isOnlyFewLeft,Boolean isInStock) {}
