package com.fakereview.review.controller;

import com.fakereview.review.dto.*;
import com.fakereview.review.model.Review;
import com.fakereview.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ReviewResponse addReview(@RequestBody ReviewRequest request,
                                    Authentication authentication) {

        String username = authentication.getName();

        return reviewService.addReview(request, username);
    }

    @GetMapping("/{productId}")
    public Page<ReviewResponse> getReviews(@PathVariable Long productId, @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size) {

        return reviewService.getReviews(productId, page, size);
    }

    @GetMapping("/count/{productId}")
    public long getReviewCount(@PathVariable Long productId) {
        return reviewService.countByProductId(productId);
    }

    @GetMapping("/rating/{productId}")
    public double getAverageRating(@PathVariable Long productId) {

        return reviewService.getAverageRating(productId);
    }

    @GetMapping("/distribution/{productId}")
    public List<RatingDistributionResponse> getRatingDistribution(
            @PathVariable Long productId) {

        return reviewService.getRatingDistribution(productId);
    }

    @GetMapping("/my-reviews")
    public ResponseEntity<?> getMyReviews(Authentication authentication) {

        String username = authentication.getName();

        List<Review> reviews = reviewService.getMyReviews(username);

        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @RequestBody UpdateReviewRequest request,
            Principal principal) {

        String username = principal.getName();

        ReviewResponse response = reviewService.updateReview(reviewId, request, username);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            Principal principal) {

        String username = principal.getName();

        reviewService.deleteReview(reviewId, username);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}/stats")
    public ResponseEntity<ProductReviewStats> getProductStats(
            @PathVariable Long productId) {

        ProductReviewStats stats = reviewService.getProductStats(productId);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/product/{productId}")
    public Page<ReviewResponse> getReviewsByProduct(
            @PathVariable Long productId,
            @RequestParam(required = false) Boolean fake,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return reviewService.getReviewsByProduct(productId, fake, page, size);
    }

    @GetMapping("/product/{productId}/top")
    public Page<ReviewResponse> getTopReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return reviewService.getTopReviews(productId, page, size);
    }

    @GetMapping("/stats")
    public Map<String,Object> getStats(){
        return reviewService.getStats();
    }
}