package com.fakereview.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateReviewRequest {

    private String reviewText;
    private int rating;
    private String imageUrl;

}