package com.fakereview.review.service;

import com.fakereview.review.dto.*;
import com.fakereview.review.model.Review;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ReviewService {

    ReviewResponse addReview(ReviewRequest request, String username);

    Page<ReviewResponse> getReviews(Long productId, int page, int size);

    long countByProductId(Long productId);

    double getAverageRating(Long productId);

    List<RatingDistributionResponse> getRatingDistribution(Long productId);

    public ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request, String username);

    public void deleteReview(Long reviewId, String username);

    public ProductReviewStats getProductStats(Long productId);

    public Page<ReviewResponse> getReviewsByProduct(
            Long productId,
            Boolean fake,
            int page,
            int size);

    public Page<ReviewResponse> getTopReviews(Long productId, int page, int size);

    Map<String, Object> getStats();

    List<Review> getMyReviews(String username);
}