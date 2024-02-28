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
public class subCatagory {

	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID subCatagoryId;
	@Id
	private String subCatagoryName;
	private String subCatagoryDescription;
	@ManyToOne
	@JoinColumn(name = "catagory")
	private Catagory catagory;
	@ManyToOne
	@JoinColumn(name = "master_catagory")
	private MasterCatagory masterCatagory;
	@OneToMany(mappedBy = "subCatagory",cascade = CascadeType.ALL)
	private List<Product> product;
}
