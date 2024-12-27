package com.company.companyapp.controller;

import com.company.companyapp.DTO.*;
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
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
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
    @PostMapping("/profile/{profileId}/company/{companyId}/update")
    public ResponseEntity<?> updateDescription(@PathVariable String profileId,@PathVariable String companyId,@RequestBody Company updatedCompany){
        Optional<Company> existingCompany = companyService.getCompanyById(updatedCompany.getId());
        if(existingCompany.isEmpty()){
            return ResponseEntity.status(HttpStatus.CREATED).body(new ErrorResponse(LocalDateTime.now(),
                    "Company with name "+updatedCompany.getName()+" not found.","Company not found"));
        }
        Company company = existingCompany.get();
        company.setDescription(updatedCompany.getDescription());
        return ResponseEntity.ok(companyService.save(company));
    }
    @PostMapping("/profile/{profileId}/company/{companyId}/demote")
    public ResponseEntity<?> demoteProfileByEmail(@PathVariable String profileId,@PathVariable String companyId, @RequestBody NewCompanyMember newCompanyMember) throws JsonProcessingException {
        String url = "http://localhost:8083/truecaller_api/profile/getProfileWithoutContactByEmail/"+newCompanyMember.getEmail();
        logger.info("in demote : ");
        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
        logger.info(response.toString());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(response.getBody());
        if (response.getStatusCode() == HttpStatus.OK) {
            ProfileWithoutContact profile = mapper.treeToValue(jsonNode, ProfileWithoutContact.class);
            rolesService.deleteWithProfileIdAndCompanyId(profile.getId(),companyId);
            return ResponseEntity.ok(new CompanyMemberProfile(profile,Role.NORMAL)); // Or return a success message/DTO
        } else {
            return response;
        }
    }
    @PostMapping("/profile/{profileId}/company/{companyId}/promote")
    public ResponseEntity<?> makeProfileModeratorByEmail(@PathVariable String profileId,@PathVariable String companyId, @RequestBody NewCompanyMember newCompanyMember) throws JsonProcessingException {
        String url = "http://localhost:8083/truecaller_api/profile/getProfileWithoutContactByEmail/"+newCompanyMember.getEmail();
        // hit a get endpoint at the above url which return in 2 different types of output
        // 1. status = 200 return ProfileWithoutcontact object
        // 2. status 201 returns ErrorResponse

        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
        logger.info(response.toString());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(response.getBody());
        if (response.getStatusCode() == HttpStatus.OK) {
            ProfileWithoutContact profile = mapper.treeToValue(jsonNode, ProfileWithoutContact.class);
            Optional<Roles> existingRelation = rolesService.findUsersRoleInCompany(profile.getId(),companyId);
            if(existingRelation.isPresent()){
                return ResponseEntity.status(HttpStatus.CREATED).body(new ErrorResponse(LocalDateTime.now(),"User is already part of the company","Account with email : "+newCompanyMember.getEmail()+" is already a member of this company."));
            }
            rolesService.save(new Roles(profile.getId(),companyId,Role.MODERATOR));
            return ResponseEntity.ok(new CompanyMemberProfile(profile,Role.MODERATOR)); // Or return a success message/DTO
        } else {
            return response;
        }
    }
    @GetMapping("/profile/{profileId}/company/{companyId}/memberManagementPage")
    public ResponseEntity<?> getListOfCompanyMemberProfiles(@PathVariable String profileId,@PathVariable String companyId){
        List<Roles> listOfRoles = rolesService.getRolesByCompanyId(companyId);
        HashMap<String,Role> profileIdToRole = new HashMap<>();
        List<String> listOfProfileIds = new ArrayList<>();
        for(Roles relation : listOfRoles){
            profileIdToRole.put(relation.getProfileId(),relation.getRole());
            listOfProfileIds.add(relation.getProfileId());
        }
        // hit the above endpoint as a post request from here and put listOfProfileIds as input
        String url = "http://localhost:8083/truecaller_api/profile/getprofilesByIds"; // Replace with your actual URL

        // Create the request body
        ListOfIds listOfIds = new ListOfIds();
        listOfIds.setListOfIds(listOfProfileIds);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Important: Set content type

        HttpEntity<ListOfIds> request = new HttpEntity<>(listOfIds, headers);

        try {
            ResponseEntity<Profile[]> response = restTemplate.postForEntity(url, request, Profile[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Profile> profiles = Arrays.asList(response.getBody());
                System.out.println("Profiles retrieved: " + profiles);
                List<CompanyMemberProfile> listOfCompanyMembers = new ArrayList<>();
                for(Profile profile : profiles){
                    listOfCompanyMembers.add(new CompanyMemberProfile(profile.convertToProfileWithoutContact(profile),profileIdToRole.get(profile.getId())));
                }
                return ResponseEntity.ok(listOfCompanyMembers); // or return a more meaningful response
            } else {
                // Handle error response
                System.err.println("Error fetching profiles: " + response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).body("Error fetching profiles"); // Return error response
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., connection errors)
            System.err.println("Exception while fetching profiles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching profiles: " + e.getMessage());
        }
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
