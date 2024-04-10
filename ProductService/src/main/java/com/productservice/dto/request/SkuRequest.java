package com.productservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class SkuRequest {

    private String size;
    private String colour;
    private BigDecimal mrp;
    private BigDecimal discountPercentage;
    private Integer quantity;
    private String availablePincodes;
    private UUID productId;
}