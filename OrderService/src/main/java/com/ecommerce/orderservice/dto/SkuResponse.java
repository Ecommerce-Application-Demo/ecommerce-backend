package com.ecommerce.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuResponse {

    private String skuId;
    private String styleName;
    private String BrandName;
    private String size;
    private List<SizeInfo> otherSizes;
    private BigDecimal mrp;
    private BigDecimal discountPercentage;
    private String discountPercentageText;
    private BigDecimal finalPrice;
    private String defaultImage;
    private Integer availableQuantity;
    private boolean isInStock;
    private Integer productCartQuantity;
    private Boolean isSelected;
}
