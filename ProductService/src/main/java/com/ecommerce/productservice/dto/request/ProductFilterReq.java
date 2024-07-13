package com.ecommerce.productservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProductFilterReq {

    @Schema(name = "masterCategories", type = "array[String]", description = "Array of selected masterCategory filter", example = "Men Clothing")
    private String[] masterCategories;

    @Schema(name = "categories", type = "array[String]", description = "Array of selected category filter", example = "Shirts, Trousers")
    private String[] categories;

    @Schema(name = "subCategories", type = "array[String]", description = "Array of selected subCategory filter", example = "Formal Shirts, Casual Trousers")
    private String[] subCategories;

    @Schema(name = "brands", type = "array[String]", description = "Array of selected brand filter", example = "Nike, Adidas")
    private String[] brands;

    @Schema(name = "gender", type = "array[String]", description = "Array of selected gender filter", example = "Men, Women")
    private String[] gender;

    @Schema(name = "colours", type = "array[String]", description = "Array of selected Colour filter", example = "Red, Black")
    private String[] colours;

    @Schema(name = "sizes", type = "array[String]", description = "Array of selected size filter", example = "M, L, XL")
    private String[] sizes;

    @Schema(name = "discountPercentage", type = "Integer", description = "Selected discount percentage filter", example = "20")
    private Integer discountPercentage;

    @Schema(name = "maxPrice", type = "Integer", description = "Maximum price filter", example = "1000")
    private Integer maxPrice;

    @Schema(name = "minPrice", type = "Integer", description = "Minimum price filter", example = "100")
    private Integer minPrice;

       
}
