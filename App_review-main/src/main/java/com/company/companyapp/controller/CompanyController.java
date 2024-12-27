package com.company.companyapp.controller;

import com.company.companyapp.DTO.CompanyDTO;
import com.company.companyapp.DTO.Profile;
import com.company.companyapp.error.ErrorResponse;
import com.company.companyapp.model.Company;
import com.company.companyapp.model.Review;
import com.company.companyapp.model.Role;
import com.company.companyapp.model.Roles;
import com.company.companyapp.repository.CompanyRepository;
import com.company.companyapp.repository.ReviewRepository;
import com.company.companyapp.service.CompanyService;
import com.company.companyapp.service.RolesService;
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
    private RolesService rolesService;
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
    @DeleteMapping("/profile/{profileId}/company/{companyId}/delete")
    public ResponseEntity<?> deleteCompany(@PathVariable String profileId,@PathVariable String companyId){
        return companyService.deleteCompany(profileId,companyId);
    }
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
                Company newCompany = companyRepository.save(companyDetails);
                Roles roles = rolesService.save(new Roles(profile.getId(), newCompany.getId(), Role.ADMIN));;
                return ResponseEntity.ok(newCompany);
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
