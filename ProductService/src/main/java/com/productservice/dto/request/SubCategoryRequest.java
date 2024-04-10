package com.productservice.dto.request;

import com.productservice.dto.CategoryDto;
import com.productservice.dto.MasterCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryRequest {
    private String SubCategoryName;
    private String SubCategoryDescription;
    private MasterCategoryDto masterCategoryDto;
    private CategoryDto categoryDto;
}
