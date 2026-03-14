package com.fakereview.review.mapper;

import com.fakereview.review.dto.ReviewResponse;
import com.fakereview.review.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(Review review) {

        return new ReviewResponse(
                review.getId(),
                review.getProductId(),
                review.getUsername(),
                review.getReviewText(),
                review.getRating(),
                review.isFake(),
                review.getFakeReason(),
                review.getImageUrl(),
                review.isVerifiedPurchase(),
                review.getCreatedAt()
        );
    }
}