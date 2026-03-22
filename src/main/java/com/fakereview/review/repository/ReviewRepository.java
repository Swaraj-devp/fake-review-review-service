package com.fakereview.review.repository;

import com.fakereview.review.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);

    List<Review> findByProductId(Long productId);

    boolean existsByProductIdAndUsername(Long productId,String username);

    long countByProductId(Long productId);


    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productId = :productId")
    Double getAverageRating(Long productId);

    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.productId = :productId GROUP BY r.rating")
    List<Object[]> getRatingDistribution(Long productId);

//    Optional<Review> findByUsernameAndProductIdAndItemType(
//            String username,
//            Long productId,
//            String itemType
//    );
    Page<Review> findByProductId(Long productId, Pageable pageable);

    Page<Review> findByProductIdAndFake(Long productId, boolean fake, Pageable pageable);

    Page<Review> findByProductIdOrderByRatingDesc(Long productId, Pageable pageable);

    long countByFakeTrue();

    List<Review> findByUsername(String username);

}