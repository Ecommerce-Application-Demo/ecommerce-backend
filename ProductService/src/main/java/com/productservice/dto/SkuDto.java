package com.productservice.dto;

import com.productservice.entity.Images;
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
    private Images images;
    private Integer quantity;
    private String availablePincodes;
}
