package com.productservice.service.declaration;

import com.productservice.dto.*;
import com.productservice.entity.ReviewRating;
import com.productservice.entity.Sku;

import java.util.UUID;

public interface ProductGetService {
    MasterCategoryDto getMasterCategory();

    CategoryDto getCategory();

    SubCategoryDto getSubCategory();

    BrandDto getBrand();

    ProductDto getProduct();

    ReviewRating getReview(UUID productId);

    Sku getSku(UUID productId);
}
