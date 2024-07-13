package com.ecommerce.productservice.dto.request;

import com.ecommerce.productservice.entity.Images;
import com.ecommerce.productservice.entity.SizeDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class StyleVariantRequest {

    private String styleId;
    private String styleName;
    private String colour;
    private String colourHexCode;
    private BigDecimal mrp;
    private BigDecimal discountPercentage;
    private Images images;
    private List<SizeDetails> sizeDetails;
    private String sizeDetailsImageUrl;
    private String productId;
}