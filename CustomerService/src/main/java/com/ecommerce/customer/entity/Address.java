package com.ecommerce.customer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int addId;
	private String userIdEmail;
	private String name;
	private String phoneNumber;
	@Column(length = 500)
	private String addressLine1;
	private String pincode;
	private String state;
	private String locality;
	private String city;
	private String addressType;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Customer addCustomer;

}