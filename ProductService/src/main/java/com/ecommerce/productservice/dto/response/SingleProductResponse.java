package com.ecommerce.productservice.dto.response;

import com.ecommerce.productservice.entity.Brand;
import com.ecommerce.productservice.entity.Gender;
import com.ecommerce.productservice.entity.Images;
import com.ecommerce.productservice.entity.Materials;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SingleProductResponse {
    private String productId;
    private String productAvgRating;
    private String reviewCount;
    private Brand brand;
    private String productDescription;
    private Gender gender;
    private Materials material;
    private String styleId;
    private String styleName;
    private String colour;
    private String colourHexCode;
    private BigDecimal mrp;
    private BigDecimal discountPercentage;
    private String discountPercentageText;
    private BigDecimal finalPrice;
    private Images images;
    private String defaultImage;
    private boolean isNewlyAdded;
    private boolean isOnlyFewLeft;
    private boolean isInStock;
    private boolean is14dayReturnable;
    private boolean isCashOnDeliveryAvailable;
    private List<BreadCrumb> breadCrumbList;
    private List<SizeInfo> sizes;
    private ReviewRatingResponse reviewRating;

}
