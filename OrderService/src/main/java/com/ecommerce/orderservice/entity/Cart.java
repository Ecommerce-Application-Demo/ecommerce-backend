package com.ecommerce.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private String browserSessionId;
    private LocalDateTime createdDate;
    @JsonIgnore
    @OneToMany(mappedBy = "cartId",cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
}
