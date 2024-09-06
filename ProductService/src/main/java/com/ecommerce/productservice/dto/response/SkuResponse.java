package com.ecommerce.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
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
}
