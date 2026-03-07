package com.fakereview.review.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "purchase-service", url = "http://localhost:8084")
public interface PurchaseFeignClient {

    @GetMapping("/api/purchases/verify")
    boolean verifyPurchase(
            @RequestParam String username,
            @RequestParam Long itemId,
            @RequestParam String itemType
    );
}
