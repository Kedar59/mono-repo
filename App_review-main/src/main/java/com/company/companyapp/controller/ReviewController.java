package com.company.companyapp.controller;

import com.company.companyapp.error.ErrorResponse;
import com.company.companyapp.model.Company;
import com.company.companyapp.model.CompanyBot;
import com.company.companyapp.model.Review;
import com.company.companyapp.service.ReviewService;
import com.company.companyapp.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/review_api/reviews")
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    @Autowired
    private CompanyService companyService;

    @PostMapping("/registerReview")
    public ResponseEntity<?> addReview(@RequestBody Review review) {
        // Find the company
        Optional<Company> companyOptional = companyService.getCompanyByName(review.getCompanyName());

        if (!companyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Company not found: " + review.getCompanyName());
        }

        Company company = companyOptional.get();

        // Calculate new rating
        float totalRating = (float) (company.getRating() * company.getNumberOfReviews()) + review.getRating();
        company.setRating(totalRating / (company.getNumberOfReviews() + 1));
        company.setNumberOfReviews(company.getNumberOfReviews() + 1);

        // Set timestamp for review
        review.setTimestamp(new Date());

        // Save both company and review
        companyService.save(company);
        Review savedReview = reviewService.save(review);

        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/getReviewsByCompanyName/{companyName}")
    public ResponseEntity<List<Review>> getReviewsByCompanyName(@PathVariable String companyName) {
        return ResponseEntity.ok(reviewService.reviewsByCompanyName(companyName));
    }
}
