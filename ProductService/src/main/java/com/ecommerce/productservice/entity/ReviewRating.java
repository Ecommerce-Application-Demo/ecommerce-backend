package com.ecommerce.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReviewRating {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String ratingId;
	private String userId;
	private String userName;
	@Column(length = 1000)
	private String review;
	private Float rating;
	private LocalDateTime createdTimestamp;
	@NotNull
	@JsonIgnore
	private String styleId;
}
