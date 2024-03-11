package com.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterCategoryDto {

    private UUID masterCategoryId;
    private String masterCategoryName;
    private String mastercategoryDescription;
}
