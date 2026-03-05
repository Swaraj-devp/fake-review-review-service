package com.fakereview.review.controller;

import com.fakereview.review.dto.ReviewRequest;
import com.fakereview.review.model.Review;
import com.fakereview.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public Review addReview(@RequestBody ReviewRequest request,
                            Authentication authentication) {

        String username = authentication.getName();

        return reviewService.addReview(request, username);
    }

    @GetMapping("/{productId}")
    public List<Review> getReviews(@PathVariable Long productId) {
        return reviewService.getReviews(productId);
    }

}