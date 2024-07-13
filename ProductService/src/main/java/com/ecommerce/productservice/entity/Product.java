package com.ecommerce.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy =GenerationType.UUID )
	private String productId;
	private String productName;
	@Column(length = 1000)
	private String productDescription;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private Materials material;
	private LocalDateTime createdTimestamp;
	@ManyToOne
	@JoinColumn(name = "product_master_category")
	private MasterCategory masterCategory;
	@ManyToOne
	@JoinColumn(name = "product_category")
	private Category category;
	@ManyToOne
	@JoinColumn(name = "product_sub_category")
	private SubCategory SubCategory;
	@ManyToOne
	@JoinColumn(name = "product_brand")
	private Brand brand;
	@JsonIgnore
	@OneToMany(mappedBy = "product")
	private List<ProductStyleVariant> productStyleVariant;
}
