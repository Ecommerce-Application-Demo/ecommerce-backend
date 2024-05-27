package com.ecommerce.productservice.dto;

import com.ecommerce.productservice.dto.response.ColourHexCode;
import com.ecommerce.productservice.entity.Brand;
import com.ecommerce.productservice.entity.MasterCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilters {

    private Set<MasterCategory> masterCategories;
    private Set<CategoryDto> categories;
    private Set<SubCategoryDto> subCategories;
    private Set<Brand> brands;
    private Set<ColourHexCode> colours;
    private Set<String> sizes;
    private Set<BigDecimal> discountPercentages;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
}
