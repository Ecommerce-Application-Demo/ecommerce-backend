package com.ecommerce.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDto {
    private String SubCategoryId;
    private String SubCategoryName;
    private String SubCategoryDescription;
    private String subCategoryDefaultImage;
    private String breadcrumbUrl;
}
