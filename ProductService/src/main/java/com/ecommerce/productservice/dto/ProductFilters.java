package com.ecommerce.productservice.dto;

import com.ecommerce.productservice.dto.response.Colours;
import com.ecommerce.productservice.dto.response.DiscountPercentage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilters {

    private Set<String> masterCategories;
    private Set<String> categories;
    private Set<String> subCategories;
    private Set<String> brands;
    private Set<String> gender;
    private Set<Colours> colours;
    private Set<String> sizes;
    private Set<DiscountPercentage> discountPercentages;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
}
