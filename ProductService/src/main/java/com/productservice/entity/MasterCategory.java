package com.productservice.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
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
public class MasterCategory {

	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID masterCategoryId;
	@Id
	private String masterCategoryName;
	private String mastercategoryDescription;
	@OneToMany(mappedBy = "masterCategory",cascade = CascadeType.ALL)
	private List<Category> category;
	@OneToMany(mappedBy = "masterCategory",cascade = CascadeType.ALL)
	private List<SubCategory> SubCategory;
	@OneToMany(mappedBy = "masterCategory",cascade = CascadeType.ALL)
	private List<Product> product;
	
}
