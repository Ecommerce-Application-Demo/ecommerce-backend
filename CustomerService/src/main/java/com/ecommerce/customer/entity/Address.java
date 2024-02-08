package com.ecommerce.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int addId;
	private String userIdEmail;
	private String name;
	private String phoneNumber;
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