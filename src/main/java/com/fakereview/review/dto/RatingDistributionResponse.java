package com.fakereview.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingDistributionResponse {

    private int rating;

    private long count;
}