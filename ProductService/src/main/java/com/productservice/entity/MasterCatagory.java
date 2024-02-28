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
public class MasterCatagory {

	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID masterCatagoryId;
	@Id
	private String masterCatagoryName;
	private String mastercatagoryDescription;
	@OneToMany(mappedBy = "masterCatagory",cascade = CascadeType.ALL)
	private List<Catagory> catagory;
	@OneToMany(mappedBy = "masterCatagory",cascade = CascadeType.ALL)
	private List<subCatagory> subCatagory;
	@OneToMany(mappedBy = "masterCatagory",cascade = CascadeType.ALL)
	private List<Product> product;
	
}
