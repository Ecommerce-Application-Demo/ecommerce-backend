package com.ecommerce.productservice.dto.response;

import com.ecommerce.productservice.entity.warehousemanagement.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTimeDetails{
    private String deliveryTime;
    private String timeDuration;
    private Warehouse warehouse;
}
