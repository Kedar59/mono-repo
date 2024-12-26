package com.company.companyapp.service;

import com.company.companyapp.model.Review;
import com.company.companyapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> reviewsByCompanyName(String companyName) {
        return reviewRepository.findByNameContaining(companyName);
    }
}
