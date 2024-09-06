package com.ecommerce.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WishlistItems {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String wishlistItemId;
    private String userId;
    private String styleId;
}
