package com.productservice.entity;

import java.math.BigDecimal;
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
	private BigDecimal price;
	private Integer quantity;
	private Long availablePincodes;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
}
