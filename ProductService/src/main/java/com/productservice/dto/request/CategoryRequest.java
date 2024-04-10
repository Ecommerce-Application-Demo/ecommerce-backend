package com.productservice.dto.request;

import com.productservice.dto.MasterCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private UUID categoryId;
    private String categoryName;
    private String categoryDescription;
    private MasterCategoryDto masterCategoryDto;
}
