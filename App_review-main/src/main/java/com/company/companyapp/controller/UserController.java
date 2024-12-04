package com.company.companyapp.controller;

import com.company.companyapp.DTO.CallerID;
import com.company.companyapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/companyMenu")
    public ResponseEntity<String> companyMenu() {
        try {
            List<String> companyNames = userService.getCompanyNamesFromCompanyService();
            if (companyNames == null || companyNames.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No companies available.");
            }

            StringBuilder formattedNames = new StringBuilder("Choose from the following companies:\n");
            for (int i = 0; i < companyNames.size(); i++) {
                formattedNames.append(i + 1).append(". ").append(companyNames.get(i)).append("\n");
            }
            return ResponseEntity.ok(formattedNames.toString());
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Failed to retrieve company names: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching company list.");
        }
    }

    @PostMapping("/reviewCompany")
    public ResponseEntity<String> reviewCompany(@RequestParam(value = "companyName") String companyName,
                                                @RequestParam(value = "review") String review,
                                                @RequestParam(value = "reviewer") String reviewer,
                                                @RequestBody CallerID callerID,  // Using CallerID instead of phoneNumber
                                                @RequestParam(value = "rating") int rating) {
        try {
            // Ensure the user is verified and matches the last verified phone number
            if (!userService.isUserVerified(callerID.getNumber()) || !callerID.getNumber().equals(userService.getLastVerifiedPhoneNumber())) {
                return ResponseEntity.badRequest().body("User not verified or phone number does not match the last verified number.");
            }

            // Check if the rating is within the allowed range
            if (rating < 1 || rating > 5) {
                return ResponseEntity.badRequest().body("Invalid rating. Please provide a rating between 1 and 5.");
            }

            Map<String, String> reviewDetails = new HashMap<>();
            reviewDetails.put("companyName", companyName);
            reviewDetails.put("review", review);
            reviewDetails.put("reviewer", reviewer);
            reviewDetails.put("phoneNumber", callerID.getNumber());  // Now using number from CallerID
            reviewDetails.put("rating", String.valueOf(rating));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(reviewDetails, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8081/api/company/receiveReview",
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            return ResponseEntity.ok("User gave response: " + response.getBody());
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Failed to submit review: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting review.");
        }
    }

}