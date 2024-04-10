package com.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDto {
    private UUID SubCategoryId;
    private String SubCategoryName;
    private String SubCategoryDescription;
}
