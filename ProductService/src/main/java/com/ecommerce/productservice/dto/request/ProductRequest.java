package com.ecommerce.productservice.dto.request;

import com.ecommerce.productservice.dto.BrandDto;
import com.ecommerce.productservice.dto.CategoryDto;
import com.ecommerce.productservice.dto.MasterCategoryDto;
import com.ecommerce.productservice.dto.SubCategoryDto;
import com.ecommerce.productservice.entity.Gender;
import com.ecommerce.productservice.entity.Materials;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String productId;
    @NotNull(message = "Must provide Product Name")
    private String productName;
    @NotNull(message = "Must provide Product Description")
    private String productDescription;
    private String productAvgRating;
    @NotNull(message = "Must provide Gender")
    private Gender gender;
    @NotNull(message = "Must provide Product material")
    private Materials material;
    @NotNull(message = "Must provide Product Master Category")
    private MasterCategoryDto masterCategory;
    @NotNull(message = "Must provide Product Category")
    private CategoryDto category;
    private SubCategoryDto SubCategory;
    @NotNull(message = "Must provide Product Brand")
    private BrandDto brand;
    private String productBreadcrumbUrl;
}
