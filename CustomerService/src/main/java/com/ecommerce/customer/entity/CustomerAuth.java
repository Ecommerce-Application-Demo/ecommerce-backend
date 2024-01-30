package com.ecommerce.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="customer_authentication")
public class CustomerAuth {

	@Id
	private String email;
	private String password;
	@OneToOne
	@JoinColumn(name = "user_id")
	private Customer authCustomer;
	
}