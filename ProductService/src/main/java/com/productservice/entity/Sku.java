package com.productservice.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Sku {

	@Id
	private String skuId;
	private String size;
	private String colour;
	private BigDecimal mrp;
	private BigDecimal discountPercentage;
	private BigDecimal finalPrice;
	private Integer quantity;
	private String availablePincodes;
	@ManyToOne
	@JoinColumn(name = "product")
	@JsonIgnore
	private Product product;
}
