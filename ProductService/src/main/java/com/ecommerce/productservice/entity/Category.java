package com.ecommerce.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {

	private String categoryId;
	@Id
	private String categoryName;
	@Column(length = 500)
	private String categoryDescription;
	private String categoryDefaultImage;
	private String cBreadcrumbUrl;
	@ManyToOne
	@JoinColumn(name = "category_master_category")
	private MasterCategory masterCategory;
	@JsonIgnore
	@OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
	private List<SubCategory> SubCategory;
	@JsonIgnore
	@OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
	private List<Product> product;
}
