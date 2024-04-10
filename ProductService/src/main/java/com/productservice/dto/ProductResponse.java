package com.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private UUID productId;
    private String productName;
    private String productDescription;
    private String productAvgRating;
    private String reviewCount;
    private String gender;
    private String material;
    private List<SkuDto> sku;
}
