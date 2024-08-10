package com.ecommerce.orderservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String order_details_id;
    private String skuId;
    private String warehouseId;
    private String inventoryId;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderDetails orderId;
}
