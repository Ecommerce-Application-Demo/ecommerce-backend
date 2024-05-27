package com.ecommerce.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortBy {
    HighToLow("psv.final_price DESC"),
    LowToHigh("psv.final_price ASC"),
    Popularity("p.product_avg_rating DESC");

    private String query;
}
