package com.ecommerce.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterCategoryDto {

    private String masterCategoryId;
    private String masterCategoryName;
    private String masterCategoryDescription;
    private String masterCategoryDefaultImage;
    private String breadcrumbUrl;

}
