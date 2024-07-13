package com.ecommerce.productservice.service.declaration;

import com.ecommerce.productservice.dto.*;
import com.ecommerce.productservice.dto.request.*;
import com.ecommerce.productservice.entity.ReviewRating;
import com.ecommerce.productservice.entity.warehousemanagement.Inventory;
import com.ecommerce.productservice.entity.warehousemanagement.Warehouse;
import com.ecommerce.productservice.exception.ProductException;

import java.util.List;

public interface ProductAddService {

    MasterCategoryDto addMasterCategory(MasterCategoryDto masterCategoryDto);

    CategoryDto addCategory(CategoryRequest categoryDto);

    SubCategoryDto addSubCategory(SubCategoryRequest subCategoryDto);

    BrandDto addBrand(BrandDto brandDto);

    ProductDto addProduct(ProductRequest productDto);

    ReviewRating addReview(ReviewRating reviewRating) throws ProductException;

    StyleVariantDetailsDto addStyleVariant(StyleVariantRequest request);

    Warehouse addWarehouse(Warehouse warehouse);

    List<Inventory> addInventory(List<InventoryReq> inventory);

    Inventory editInventory(Inventory inventory);
}
