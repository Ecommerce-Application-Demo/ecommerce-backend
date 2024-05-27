package com.ecommerce.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Materials implements Serializable {
    private String fabric;
    private Object otherDetails;
}
