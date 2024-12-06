package com.company.companyapp.repository;

import com.company.companyapp.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
//    List<Review> findByCompanyId(String companyId);  // Fetch reviews by companyId
    @Query("{ 'companyName': { $regex: ?0, $options: 'i' } }")
    List<Review> findByNameContaining(String companyName);
}
