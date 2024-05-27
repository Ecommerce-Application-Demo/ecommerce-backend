package com.ecommerce.productservice.dto.request;

import com.ecommerce.productservice.dto.MasterCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private String categoryId;
    private String categoryName;
    private String categoryDescription;
    private String categoryDefaultImage;
    private String breadcrumbUrl;
    private MasterCategoryDto masterCategoryDto;
}
