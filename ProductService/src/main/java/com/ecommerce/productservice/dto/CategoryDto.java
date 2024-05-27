package com.ecommerce.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private String categoryId;
    private String categoryName;
    private String categoryDescription;
    private String categoryDefaultImage;
    private String breadcrumbUrl;

}
