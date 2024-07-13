package com.ecommerce.productservice.dto.response;

import com.ecommerce.productservice.dto.*;
import com.ecommerce.productservice.entity.Gender;
import com.ecommerce.productservice.entity.Materials;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private String productId;
    private String productName;
    private String productDescription;
    private Gender gender;
    private Materials material;
    private String breadcrumbUrl;
    private MasterCategoryDto masterCategory;
    private CategoryDto category;
    private SubCategoryDto SubCategory;
    private BrandDto brand;
    private List<BreadCrumb> breadCrumbList;
    private List<StyleVariantDetailsDto> styleVariants;
}
