package com.ecommerce.orderservice.dto;

import lombok.Data;

@Data
public class CartRequest {

    private String skuId;
    private Integer quantity;
    private Boolean isSelected = true;
}
