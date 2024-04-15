package com.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SubCategory {

	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID SubCategoryId;
	@Id
	private String SubCategoryName;
	@Column(length = 500)
	private String SubCategoryDescription;
	@ManyToOne
	@JoinColumn(name = "sub_category_category")
	private Category category;
	@ManyToOne
	@JoinColumn(name = "sub_category_master_category")
	private MasterCategory masterCategory;
	@OneToMany(mappedBy = "SubCategory",cascade = CascadeType.ALL)
	private List<Product> product;
}
