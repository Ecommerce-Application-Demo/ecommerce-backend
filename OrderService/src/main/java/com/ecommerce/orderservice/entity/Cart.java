package com.ecommerce.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cartId;
    private String userId;
    private String deviceId;
    @OneToMany(mappedBy = "cartId",cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
}
