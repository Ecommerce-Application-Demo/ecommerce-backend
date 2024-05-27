package com.ecommerce.productservice.dto.request;

import com.ecommerce.productservice.dto.CategoryDto;
import com.ecommerce.productservice.dto.MasterCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryRequest {
    private String subCategoryId;
    private String subCategoryName;
    private String subCategoryDescription;
    private String subCategoryDefaultImage;
    private String breadcrumbUrl;
    private MasterCategoryDto masterCategoryDto;
    private CategoryDto categoryDto;
}
