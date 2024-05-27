package com.ecommerce.productservice.service.declaration;

import com.ecommerce.productservice.dto.*;
import com.ecommerce.productservice.dto.request.CategoryRequest;
import com.ecommerce.productservice.dto.request.ProductRequest;
import com.ecommerce.productservice.dto.request.StyleVariantRequest;
import com.ecommerce.productservice.dto.request.SubCategoryRequest;
import com.ecommerce.productservice.entity.ProductStyleVariant;
import com.ecommerce.productservice.entity.ReviewRating;
import com.ecommerce.productservice.entity.warehousemanagement.Inventory;
import com.ecommerce.productservice.entity.warehousemanagement.Warehouse;

import java.util.List;

public interface ProductAddService {

    MasterCategoryDto addMasterCategory(MasterCategoryDto masterCategoryDto);

    CategoryDto addCategory(CategoryRequest categoryDto);

    SubCategoryDto addSubCategory(SubCategoryRequest subCategoryDto);

    BrandDto addBrand(BrandDto brandDto);

    ProductDto addProduct(ProductRequest productDto);

    ReviewRating addReview(ReviewRating reviewRating);

    ProductStyleVariant addStyleVariant(StyleVariantRequest request);

    Warehouse addWarehouse(Warehouse warehouse);

    List<Inventory> addInventory(List<Inventory> inventory);
}
