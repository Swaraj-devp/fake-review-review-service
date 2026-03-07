package com.fakereview.review.serviceImpl;

import com.fakereview.review.client.DetectionClient;
import com.fakereview.review.client.PurchaseFeignClient;
import com.fakereview.review.dto.DetectionRequest;
import com.fakereview.review.dto.DetectionResponse;
import com.fakereview.review.dto.ReviewRequest;
import com.fakereview.review.model.Review;
import com.fakereview.review.repository.ReviewRepository;
import com.fakereview.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private DetectionClient detectionClient;

    @Autowired
    private PurchaseFeignClient purchaseFeignClient;

    @Override
    public Review addReview(ReviewRequest request, String username) {

        Review review = new Review();

        review.setProductId(request.getProductId());
        review.setReviewText(request.getReviewText());
        review.setRating(request.getRating());
        review.setImageUrl(request.getImageUrl());
        review.setUsername(username);

        boolean verifiedPurchase = purchaseFeignClient.verifyPurchase(
                username,
                request.getProductId(),
                request.getItemType()
        );

        review.setVerifiedPurchase(verifiedPurchase);

        DetectionRequest detectionRequest = new DetectionRequest();
        detectionRequest.setProductId(request.getProductId());
        detectionRequest.setUsername(username);
        detectionRequest.setReviewText(request.getReviewText());
        detectionRequest.setRating(request.getRating());

        DetectionResponse response = detectionClient.analyzeReview(detectionRequest);

        review.setFake(response.isFake());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviews(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
