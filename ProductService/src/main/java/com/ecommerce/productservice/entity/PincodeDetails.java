package com.ecommerce.productservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PincodeDetails {
    @Id
    private String pincode;
    private Float latitude;
    private Float longitude;
}
