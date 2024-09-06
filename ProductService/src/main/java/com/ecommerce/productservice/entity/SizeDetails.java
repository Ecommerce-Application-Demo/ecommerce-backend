package com.ecommerce.productservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SizeDetails {

    @Id
    private String skuId;
    private String size;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "psv_id")
    private ProductStyleVariant psv_id;
}

