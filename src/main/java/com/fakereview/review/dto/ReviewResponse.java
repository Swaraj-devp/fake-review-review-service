package com.fakereview.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private Long id;

    private Long productId;

    private String username;

    private String reviewText;

    private int rating;

    private boolean fake;

    private String fakeReason;

    private String imageUrl;

    private boolean verifiedPurchase;

    private LocalDateTime createdAt;

}
