package com.productservice.service.declaration;

import com.productservice.dto.*;
import com.productservice.dto.request.CategoryRequest;
import com.productservice.dto.request.ProductRequest;
import com.productservice.dto.request.SkuRequest;
import com.productservice.dto.request.SubCategoryRequest;
import com.productservice.entity.ReviewRating;
import com.productservice.entity.Sku;

public interface ProductAddService {

    void addMasterCategory(MasterCategoryDto masterCategoryDto);

    void addCategory(CategoryRequest categoryDto);

    void addSubCategory(SubCategoryRequest subCategoryDto);

    void addBrand(BrandDto brandDto);

    ProductDto addProduct(ProductRequest productDto);

    ReviewRating addReview(ReviewRating reviewRating);

    Sku addSku(SkuRequest sku);
}
