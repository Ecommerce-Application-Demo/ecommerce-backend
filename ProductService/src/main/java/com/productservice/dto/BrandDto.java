package com.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {

    private UUID brandId;
    private String brandName;
    private String brandDescription;
    private String brandAddress;
}
