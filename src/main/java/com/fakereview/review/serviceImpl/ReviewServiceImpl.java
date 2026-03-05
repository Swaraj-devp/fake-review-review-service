package com.fakereview.review.serviceImpl;

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

    @Override
    public Review addReview(ReviewRequest request,String username) {

        // Duplicate review check
        boolean alreadyReviewed =
                reviewRepository.existsByProductIdAndUsername(
                        request.getProductId(), username);

        if(alreadyReviewed){
            throw new RuntimeException("User already reviewed this product");
        }

        Review review = new Review();

        review.setProductId(request.getProductId());
        review.setUsername(username);
        review.setReviewText(request.getReviewText());
        review.setRating(request.getRating());

        // Temporary detection
        review.setFake(detectFakeReview(request));

        review.setVerifiedPurchase(false);

        return reviewRepository.save(review);
    }

    @Override
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
