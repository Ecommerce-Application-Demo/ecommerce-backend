package com.ecommerce.productservice.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SizeDetails {

    private String sizeId;
    private String size;
    private Integer quantity;
}
