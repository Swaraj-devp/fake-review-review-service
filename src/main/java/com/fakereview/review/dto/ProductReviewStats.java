package com.fakereview.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductReviewStats {

    private Long productId;
    private Double averageRating;
    private long totalReviews;

}