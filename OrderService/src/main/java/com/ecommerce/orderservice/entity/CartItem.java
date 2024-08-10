package com.ecommerce.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {

    @Id
    private String cartItemId;
    private String skuId;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cartId;

}
