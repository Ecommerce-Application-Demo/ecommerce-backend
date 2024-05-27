package com.ecommerce.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReviewRating {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String ratingId;
	private String customerEmail;
	private String customerName;
	@Column(length = 1000)
	private String review;
	private Float rating;
	private String productId;
}
