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
public class Category {

	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID categoryId;
	@Id
	private String categoryName;
	@Column(length = 500)
	private String categoryDescription;
	@ManyToOne
	@JoinColumn(name = "master_category")
	private MasterCategory masterCategory;
	@OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
	private List<SubCategory> SubCategory;
	@OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
	private List<Product> product;
}
