package com.ecommerce.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CustomerAuth {

	@Id
	private String email;
	private String password;
	@OneToOne
	@JoinColumn(name = "user_id")
	private Customer authCustomer;
	
}