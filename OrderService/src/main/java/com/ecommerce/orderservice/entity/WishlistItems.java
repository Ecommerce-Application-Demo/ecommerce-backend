package com.ecommerce.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class WishlistItems {

    @Id
    private String wishlistItemId;
    private String userId;
    private String styleId;
}
