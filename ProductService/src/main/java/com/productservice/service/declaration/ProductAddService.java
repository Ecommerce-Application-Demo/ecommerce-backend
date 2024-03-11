package com.productservice.service.declaration;

import com.productservice.dto.*;
import com.productservice.entity.ReviewRating;
import com.productservice.entity.Sku;

public interface ProductAddService {

    void addMasterCategory(MasterCategoryDto masterCategoryDto);

    void addCategory(CategoryDto categoryDto);

    void addSubCategory(SubCategoryDto subCategoryDto);

    void addBrand(BrandDto brandDto);

    ProductDto addProduct(ProductDto productDto);

    ReviewRating addReview(ReviewRating reviewRating);

    Sku addSku(Sku sku);
}
