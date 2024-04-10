package com.productservice.dto.request;

import com.productservice.dto.BrandDto;
import com.productservice.dto.CategoryDto;
import com.productservice.dto.MasterCategoryDto;
import com.productservice.dto.SubCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String productName;
    private String productDescription;
    private String productAvgRating;
    private String gender;
    private String material;
    private MasterCategoryDto masterCategory;
    private CategoryDto category;
    private SubCategoryDto SubCategory;
    private BrandDto brand;
}
