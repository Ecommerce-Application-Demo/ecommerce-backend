package com.ecommerce.orderservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;
    private int userId;
    @OneToMany(mappedBy = "orderId",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private String shippingAddressId;
    private String shippingAddress;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String paymentStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
