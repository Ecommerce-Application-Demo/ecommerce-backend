package com.productservice.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private UUID ratingId;
	private String customerEmail;
	private String customerName;
	private String review;
	private Float rating;
	private UUID productId;
}
