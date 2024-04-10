package com.productservice.service.declaration;

import com.productservice.dto.*;
import com.productservice.entity.ReviewRating;
import com.productservice.entity.Sku;

import java.util.List;
import java.util.UUID;

public interface ProductGetService {
  //  List<MasterCategoryDto> getAllMasterCategory();

    List<MasterCategoryDto> getMasterCategory(String masterCategoryId, String masterCategoryName);

    List<CategoryDto> getCategory(String categoryName, String categoryId, String masterCategory);

    List<SubCategoryDto> getSubCategory(String subCategoryName, String subCategoryId, String categoryName);

    List<BrandDto> getBrand();

    List<ProductDto> getProduct(String productId, String productName, String subCategoryName, String categoryName, String masterCategoryName, String brand, String gender);

    List<ReviewRating> getReview(UUID productId);

    List<SkuDto> getSku(String productId,String skuId, String size, String colour );

}
