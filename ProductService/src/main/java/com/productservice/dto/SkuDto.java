package com.productservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SkuDto {

    private String skuId;
    private String size;
    private String colour;
    private BigDecimal mrp;
    private BigDecimal discountPercentage;
    private BigDecimal finalPrice;
    private Integer quantity;
    private String availablePincodes;
}
