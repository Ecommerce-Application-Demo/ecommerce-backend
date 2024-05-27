package com.ecommerce.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {

    private String brandId;
    private String brandName;
    private String brandDescription;
    private String brandLogoImage;
    private String brandAddress;

}
