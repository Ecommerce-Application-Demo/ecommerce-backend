package com.ecommerce.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Brand {

	private String brandId;
	@Id
	private String brandName;
	@Column(length = 500)
	private String brandDescription;
	private String brandAddress;
	private String brandLogoImage;
	@OneToMany(mappedBy = "brand")
	@JsonIgnore
	private List<Product> productId;
}
