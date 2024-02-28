package com.productservice.entity;

import java.util.List;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Brand {

	@GeneratedValue(strategy =GenerationType.UUID )
	private UUID brandId;
	@Id
	private String brandName;
	private String brandDescription;
	private String brandAddress;
	@OneToMany(mappedBy = "brand")
	private List<Product> productId;
}
