package com.productservice.entity;

import java.util.List;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy =GenerationType.UUID )
	private UUID productId;
	private String productName;
	private String productDescription;
	private String productAvgRating;
	private String gender;
	private String material;
	@ManyToOne
	@JoinColumn(name = "master_catagory")
	private MasterCatagory masterCatagory;
	@ManyToOne
	@JoinColumn(name = "catagory")
	private Catagory catagory;
	@ManyToOne
	@JoinColumn(name = "sub_catagory")
	private subCatagory subCatagory;
	@ManyToOne
	@JoinColumn(name = "brand")
	private Brand brand;
	@OneToMany(mappedBy = "product")
	private List<Sku> sku;
}
