package com.ecommerce.productservice.service.declaration;

import com.ecommerce.productservice.dto.*;
import com.ecommerce.productservice.dto.response.ProductResponse;
import com.ecommerce.productservice.entity.ReviewRating;
import com.ecommerce.productservice.entity.warehousemanagement.Inventory;
import com.ecommerce.productservice.entity.warehousemanagement.Warehouse;

import java.util.List;

public interface ProductGetService {

    List<MasterCategoryDto> getMasterCategory(String masterCategoryId, String masterCategoryName);

    List<CategoryDto> getCategory(String categoryName, String categoryId, String masterCategory);

    List<SubCategoryDto> getSubCategory(String subCategoryName, String subCategoryId, String categoryName);

    List<BrandDto> getBrand();

    List<ProductResponse> getProduct(String productId, String productName, String subCategoryName, String categoryName, String masterCategoryName, String brand, String gender);

    List<ReviewRating> getReview(String productId);

    List<StyleVariantDetailsDto> getStyleVariants(String productId, String styleId);

    List<Warehouse> getWarehouse(Integer warehouseId);

    List<Inventory> getInventory(String skuId);
}
