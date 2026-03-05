package com.fakereview.review.service;

import com.fakereview.review.dto.ReviewRequest;
import com.fakereview.review.model.Review;
import com.fakereview.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review addReview(ReviewRequest request,String username) {

        Review review = new Review();

        review.setProductId(request.getProductId());
        review.setReviewText(request.getReviewText());
        review.setRating(request.getRating());
        review.setUsername(username);

        // Fake review detection
        review.setFake(detectFakeReview(request));

        return reviewRepository.save(review);
    }

    public List<Review> getReviews(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    private boolean detectFakeReview(ReviewRequest request) {

        if (request.getReviewText().length() < 10) {
            return true;
        }

        if (request.getRating() == 5 && request.getReviewText().length() < 15) {
            return true;
        }

        return false;
    }

}
