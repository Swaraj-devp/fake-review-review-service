package com.fakereview.review.service;

import com.fakereview.review.dto.ReviewRequest;
import com.fakereview.review.model.Review;

import java.util.List;

public interface ReviewService {

    public Review addReview(ReviewRequest request, String username);

    public List<Review> getReviews(Long productId);
}
