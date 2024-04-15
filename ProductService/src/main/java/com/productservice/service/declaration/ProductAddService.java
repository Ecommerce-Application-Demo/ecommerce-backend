package com.productservice.service.declaration;

import com.productservice.dto.*;
import com.productservice.dto.request.CategoryRequest;
import com.productservice.dto.request.ProductRequest;
import com.productservice.dto.request.SkuRequest;
import com.productservice.dto.request.SubCategoryRequest;
import com.productservice.entity.ReviewRating;
import com.productservice.entity.Sku;

public interface ProductAddService {

    MasterCategoryDto addMasterCategory(MasterCategoryDto masterCategoryDto);

    CategoryDto addCategory(CategoryRequest categoryDto);

    SubCategoryDto addSubCategory(SubCategoryRequest subCategoryDto);

    BrandDto addBrand(BrandDto brandDto);

    ProductDto addProduct(ProductRequest productDto);

    ReviewRating addReview(ReviewRating reviewRating);

    Sku addSku(SkuRequest sku);
}
