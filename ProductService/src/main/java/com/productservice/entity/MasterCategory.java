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
public class MasterCategory {

	private UUID masterCategoryId;
	@Id
	private String masterCategoryName;
	@Column(length = 500)
	private String mastercategoryDescription;
	@OneToMany(mappedBy = "masterCategory",cascade = CascadeType.ALL)
	private List<Category> category;
	@OneToMany(mappedBy = "masterCategory",cascade = CascadeType.ALL)
	private List<SubCategory> SubCategory;
	@OneToMany(mappedBy = "masterCategory",cascade = CascadeType.ALL)
	private List<Product> product;
	
}
