package com.ecommerce.productservice.dto.response;

import com.ecommerce.productservice.entity.ReviewRating;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReviewRatingResponse {

    @Schema(name = "countPerRating", example = """
             [
                {
                    "ratingcount": 4,
                    "rating": 3.0
                } \
            ]\s""")
    private List<Map> countPerRating;
    private List<ReviewRating> allReviewAndRating;
    private String productAvgRating;
    private String reviewCount;
}
