package com.ecommerce.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductStyleVariant {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String styleId;
	private String styleName;
	private String colour;
	private String colourHexCode;
	private BigDecimal mrp;
	private BigDecimal discountPercentage;
	private String discountPercentageText;
	private BigDecimal finalPrice;
	private String sizeDetailsImageUrl;
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private Images images;
	private Float productAvgRating;
	private Long reviewCount;
	private LocalDateTime createdTimeStamp;
	@OneToMany(mappedBy = "psv_id")
	@JsonIgnore
	private List<SizeDetails> sizeDetails;
	@ManyToOne
	@JoinColumn(name = "psv_product")
	private Product product;
}
