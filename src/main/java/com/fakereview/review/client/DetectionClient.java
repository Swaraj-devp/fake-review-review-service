package com.fakereview.review.client;

import com.fakereview.review.dto.DetectionRequest;
import com.fakereview.review.dto.DetectionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "detection-service", url = "http://localhost:8083")
public interface DetectionClient {

    @PostMapping("/api/detection/analyze")
    DetectionResponse analyzeReview(@RequestBody DetectionRequest request);
}
