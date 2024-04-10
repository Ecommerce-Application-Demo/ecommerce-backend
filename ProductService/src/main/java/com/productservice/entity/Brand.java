package com.productservice.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Brand {

	private UUID brandId;
	@Id
	private String brandName;
	@Column(length = 500)
	private String brandDescription;
	private String brandAddress;
	@OneToMany(mappedBy = "brand")
	private List<Product> productId;
}
