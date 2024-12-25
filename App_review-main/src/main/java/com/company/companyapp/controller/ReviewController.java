package com.company.companyapp.controller;

import com.company.companyapp.error.ErrorResponse;
import com.company.companyapp.model.Company;
import com.company.companyapp.model.CompanyBot;
import com.company.companyapp.model.Review;
import com.company.companyapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/review_api/reviews")
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    @PostMapping("/registerReview")
    public ResponseEntity<Review> addCompanyBot(@RequestBody Review review) {
        review.setTimestamp(new Date());
        return ResponseEntity.ok(reviewService.save(review));
    }

    @GetMapping("/getReviewsByCompanyName/{companyName}")
    public ResponseEntity<List<Review>> getReviewsByCompanyName(@PathVariable String companyName) {
        return ResponseEntity.ok(reviewService.reviewsByCompanyName(companyName));
    }
}
