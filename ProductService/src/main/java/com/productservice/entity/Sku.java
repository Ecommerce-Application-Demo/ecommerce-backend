package com.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

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
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private Images images;
	private Integer quantity;
	private String availablePincodes;
	@ManyToOne
	@JoinColumn(name = "sku_product")
	@JsonIgnore
	private Product product;
}
