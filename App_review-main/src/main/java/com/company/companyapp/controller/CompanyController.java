package com.company.companyapp.controller;

import com.company.companyapp.DTO.CompanyDTO;
import com.company.companyapp.DTO.Profile;
import com.company.companyapp.error.ErrorResponse;
import com.company.companyapp.model.Company;
import com.company.companyapp.model.Review;
import com.company.companyapp.repository.CompanyRepository;
import com.company.companyapp.repository.ReviewRepository;
import com.company.companyapp.service.CompanyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/review_api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    // Endpoint to fetch all company names
    @GetMapping("/allCompanies")
    public ResponseEntity<List<CompanyDTO>> getAllCompanyNames() {
        List<CompanyDTO> companies = companyService.getAllCompanies(); // Using service layer
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/searchFor/{companyName}")
    public ResponseEntity<List<CompanyDTO>> searchCompanies(@PathVariable String companyName) {
        List<CompanyDTO> companies = companyService.searchForCompanies(companyName); // Using service layer
        return ResponseEntity.ok(companies);
    }

    // Endpoint to receive a review for a company
    // @PostMapping("/receiveReview")
    // public ResponseEntity<String> receiveReview(@RequestBody Map<String, String>
    // reviewDetails) {
    // String companyName = reviewDetails.get("companyName");
    // String review = reviewDetails.get("review");
    // String reviewer = reviewDetails.get("reviewer");
    // String phoneNumber = reviewDetails.get("phoneNumber"); // This should now be
    // passed from CallerID
    // int rating = Integer.parseInt(reviewDetails.get("rating"));
    //
    // Optional<Company> optionalCompany =
    // companyRepository.findByName(companyName);o
    // if (optionalCompany.isPresent()) {
    // Company company = optionalCompany.get();
    // String companyId = company.getId();
    //
    // // Create and save the review
    // Review newReview = new Review(companyId, reviewer, phoneNumber, review,
    // rating);
    // reviewRepository.save(newReview);
    //
    // // Update the company's average rating
    // updateCompanyRating(companyId);
    //
    // return ResponseEntity.ok("Review added successfully!");
    // } else {
    // return ResponseEntity.badRequest().body("Company not found");
    // }
    // }

    // private void updateCompanyRating(String companyId) {
    // List<Review> reviews = reviewRepository.findByCompanyId(companyId);
    // double newAverageRating = reviews.stream()
    // .mapToInt(Review::getRating)
    // .average()
    // .orElse(0.0);
    //
    // Company company = companyRepository.findById(companyId).orElseThrow();
    // company.setRating(newAverageRating);
    // companyRepository.save(company);
    // }

    // @GetMapping("/{companyId}/reviews")
    // public ResponseEntity<List<Review>> getReviewsByCompany(@PathVariable String
    // companyId) {
    // List<Review> reviews = reviewRepository.findByCompanyId(companyId);
    //
    // if (reviews.isEmpty()) {
    // return ResponseEntity.noContent().build();
    // }
    //
    // return ResponseEntity.ok(reviews);
    // }

    // Modified addCompany endpoint to prevent duplicate companies
    @PostMapping("/addCompany")
    public ResponseEntity<?> addCompany(@RequestBody Company companyDetails) {
        Optional<Company> existingCompany = companyRepository.findByName(companyDetails.getName());
        if (existingCompany.isPresent()) {
            return ResponseEntity.status(HttpStatus.FOUND).body("Company already exists!");
        }

        companyDetails.setNumberOfReviews(0); // Initialize number of reviews
        companyDetails.setRating(0.0); // Initialize rating

        String url = "http://localhost:8083/truecaller_api/profile/getProfileByEmail/" + companyDetails.getOwnerEmail();

        logger.info("url -> " + url);
        try {
            // Make the API call
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            logger.info("response : " + response);
            // Initialize the ObjectMapper to map JSON dynamically
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.valueToTree(response.getBody());
            // Map response to Profile
            Profile profile = mapper.treeToValue(jsonNode, Profile.class);
            logger.info("Received Profile response: " + profile);
            if (profile.isVerified()) {
                return ResponseEntity.ok(companyRepository.save(companyDetails));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Please get your profile with email -> "
                        + companyDetails.getOwnerEmail() + " verified");
            }
        } catch (RestClientException e) {
            // Handle any REST client exceptions
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Error retrieving profile: " + e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
