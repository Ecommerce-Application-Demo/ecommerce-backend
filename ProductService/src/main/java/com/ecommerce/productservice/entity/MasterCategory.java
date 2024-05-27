package com.ecommerce.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MasterCategory {

	private String masterCategoryId;
	@Id
	private String masterCategoryName;
	@Column(length = 500)
	private String masterCategoryDescription;
	private String masterCategoryDefaultImage;
	private String mcBreadcrumbUrl;
	@JsonIgnore
	@OneToMany(mappedBy = "masterCategory",cascade = CascadeType.ALL)
	private List<Category> category;
	@JsonIgnore
	@OneToMany(mappedBy = "masterCategory",cascade = CascadeType.ALL)
	private List<SubCategory> SubCategory;
	@JsonIgnore
	@OneToMany(mappedBy = "masterCategory",cascade = CascadeType.ALL)
	private List<Product> product;
	
}
