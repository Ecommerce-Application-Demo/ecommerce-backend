package com.ecommerce.productservice.dto;

import com.ecommerce.productservice.entity.Materials;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String productId;
    private String productName;
    private String productDescription;
    private String gender;
    private Materials material;
    private String breadcrumbUrl;
    private MasterCategoryDto masterCategory;
    private CategoryDto category;
    private SubCategoryDto SubCategory;
    private BrandDto brand;
}
