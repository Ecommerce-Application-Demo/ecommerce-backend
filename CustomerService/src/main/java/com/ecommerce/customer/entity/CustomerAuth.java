package com.ecommerce.customer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_authentication")
public class CustomerAuth {

	@Id
	private String email;
	private String password;
	private String loginSalt;
	private Boolean isEnabled=true;
	@OneToOne
	@JoinColumn(name = "user_id")
	private Customer authCustomer;
	
}