package com.fakereview.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetectionRequest {

    private Long productId;
    private String username;
    private String reviewText;
    private int rating;
}