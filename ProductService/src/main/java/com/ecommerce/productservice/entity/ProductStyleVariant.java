package com.ecommerce.productservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
	@ElementCollection
	@CollectionTable(name = "size_details",joinColumns = @JoinColumn(name ="psv_id"))
	private List<SizeDetails> sizeDetails = new ArrayList<>();
	private String sizeDetailsImageUrl;
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private Images images;
	private Float productAvgRating;
	private Long reviewCount;
	private LocalDateTime createdTimeStamp;
	@ManyToOne
	@JoinColumn(name = "psv_product")
	private Product product;
}
