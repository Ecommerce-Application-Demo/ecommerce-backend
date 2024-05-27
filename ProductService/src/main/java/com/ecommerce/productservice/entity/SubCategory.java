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
public class SubCategory {

	private String SubCategoryId;
	@Id
	private String SubCategoryName;
	@Column(length = 500)
	private String SubCategoryDescription;
	private String SubCategoryDefaultImage;
	private String scBreadcrumbUrl;
	@ManyToOne
	@JoinColumn(name = "sub_category_category")
	private Category category;
	@ManyToOne
	@JoinColumn(name = "sub_category_master_category")
	private MasterCategory masterCategory;
	@JsonIgnore
	@OneToMany(mappedBy = "SubCategory",cascade = CascadeType.ALL)
	private List<Product> product;
}
