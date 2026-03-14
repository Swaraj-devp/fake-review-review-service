package com.fakereview.review.serviceImpl;

import com.fakereview.review.client.DetectionClient;
import com.fakereview.review.client.PurchaseFeignClient;
import com.fakereview.review.dto.*;
import com.fakereview.review.exception.DuplicateReviewException;
import com.fakereview.review.exception.ReviewNotFoundException;
import com.fakereview.review.exception.UnauthorizedReviewAccessException;
import com.fakereview.review.mapper.ReviewMapper;
import com.fakereview.review.model.Review;
import com.fakereview.review.repository.ReviewRepository;
import com.fakereview.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private DetectionClient detectionClient;

    @Autowired
    private PurchaseFeignClient purchaseFeignClient;

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public ReviewResponse addReview(ReviewRequest request, String username) {

        log.info("User {} is adding review for product {}", username, request.getProductId());

        if (reviewRepository.existsByProductIdAndUsername(request.getProductId(), username)) {
            log.warn("Duplicate review attempt by user {} for product {}", username, request.getProductId());

            throw new DuplicateReviewException("User already reviewed this product");
        }

        Review review = new Review();

        review.setProductId(request.getProductId());
        review.setReviewText(request.getReviewText());
        review.setRating(request.getRating());
        review.setImageUrl(request.getImageUrl());
        review.setUsername(username);
        review.setCreatedAt(LocalDateTime.now());
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
        review.setFakeReason(response.getReason());

        Review saved = reviewRepository.save(review);
        log.info("Review successfully saved for product {}", request.getProductId());
        return reviewMapper.toResponse(saved);
    }

    @Override
    public Page<ReviewResponse> getReviews(Long productId, int page, int size) {

        log.info("Fetching reviews for product {} page {} size {}", productId, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<Review> reviews =
                reviewRepository.findByProductIdOrderByCreatedAtDesc(productId, pageable);

        return reviews.map(reviewMapper::toResponse);
    }


    @Override
    public long countByProductId(Long productId) {
        return reviewRepository.countByProductId(productId);
    }

    @Override
    public double getAverageRating(Long productId) {

        Double avg = reviewRepository.getAverageRating(productId);

        return avg != null ? avg : 0.0;
    }

    @Override
    public List<RatingDistributionResponse> getRatingDistribution(Long productId) {

        List<Object[]> results = reviewRepository.getRatingDistribution(productId);

        List<RatingDistributionResponse> distribution = new ArrayList<>();

        for (Object[] row : results) {

            int rating = (Integer) row[0];
            long count = (Long) row[1];

            distribution.add(new RatingDistributionResponse(rating, count));
        }

        return distribution;
    }

    @Override
    public ReviewResponse getMyReview(Long productId, String itemType) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Review review = reviewRepository
                .findByUsernameAndProductIdAndItemType(
                        username,
                        productId,
                        itemType
                )
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));
        return reviewMapper.toResponse(review);
    }

    @Override
    public ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request, String username) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        if (!review.getUsername().equals(username)) {
            throw new UnauthorizedReviewAccessException("You can update only your review");
        }

        review.setReviewText(request.getReviewText());
        review.setRating(request.getRating());
        review.setImageUrl(request.getImageUrl());

        Review updated = reviewRepository.save(review);

        return reviewMapper.toResponse(updated);
    }

    @Override
    public void deleteReview(Long reviewId, String username) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUsername().equals(username)) {
            throw new RuntimeException("You can delete only your review");
        }

        reviewRepository.delete(review);
    }

    @Override
    public ProductReviewStats getProductStats(Long productId) {

        Double avgRating = reviewRepository.getAverageRating(productId);
        long total = reviewRepository.countByProductId(productId);

        if (avgRating == null) {
            avgRating = 0.0;
        }

        return new ProductReviewStats(productId, avgRating, total);
    }

    @Override
    public Page<ReviewResponse> getReviewsByProduct(
            Long productId,
            Boolean fake,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Review> reviews;

        if (fake == null) {
            reviews = reviewRepository.findByProductId(productId, pageable);
        } else {
            reviews = reviewRepository.findByProductIdAndFake(productId, fake, pageable);
        }

        return reviews.map(reviewMapper::toResponse);
    }

    @Override
    public Page<ReviewResponse> getTopReviews(Long productId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Review> reviews =
                reviewRepository.findByProductIdOrderByRatingDesc(productId, pageable);

        return reviews.map(reviewMapper::toResponse);
    }
}