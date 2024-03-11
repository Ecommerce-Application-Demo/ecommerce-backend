package com.productservice.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
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
public class SubCategory {

	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID SubCategoryId;
	@Id
	private String SubCategoryName;
	private String SubCategoryDescription;
	@ManyToOne
	@JoinColumn(name = "category")
	private Category category;
	@ManyToOne
	@JoinColumn(name = "master_category")
	private MasterCategory masterCategory;
	@OneToMany(mappedBy = "SubCategory",cascade = CascadeType.ALL)
	private List<Product> product;
}
