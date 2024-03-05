package com.ecommerce.customer.entity;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	@Column(unique = true)
	private String email;
	private String name;
	@Transient
	private String password;
	private String phoneNumber;
	private String gender;
	@OneToMany(mappedBy = "addCustomer" ,cascade = CascadeType.ALL)
	private List<Address> address;
	@OneToOne(mappedBy = "authCustomer",cascade = CascadeType.ALL)
	private CustomerAuth customerAuth;
	

}
