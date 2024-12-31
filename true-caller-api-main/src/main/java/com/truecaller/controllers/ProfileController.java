package com.truecaller.controllers;

import com.truecaller.entities.Profile;
import com.truecaller.error.ErrorResponse;
import com.truecaller.exceptions.ProfileNotFoundException;
import com.truecaller.projections.*;
import com.truecaller.services.OtpService;
import com.truecaller.services.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/truecaller_api/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private OtpService otpService;
    private Logger logger = LoggerFactory.getLogger(ProfileController.class);
    @PostMapping("/getprofilesByIds")
    public ResponseEntity<List<Profile>> getProfilesByIds(@RequestBody ListOfIds listOfIds){
        return ResponseEntity.ok(profileService.getListOfProfiles(listOfIds.getListOfIds()));
    }


    @GetMapping("/getProfileByEmail/{email}")
    public ResponseEntity<?> getProfileByEmail(@PathVariable String email){
        Optional<Profile> existingProfile = profileService.getProfileByEmail(email);
        logger.info("email : "+email);
        if(existingProfile.isEmpty()){
            return ResponseEntity.status(HttpStatus.CREATED).body(new ErrorResponse("Register a account with email : "+email,"Profile with email \"+email+\" not found"));
        }
        Profile profile = existingProfile.get();
        ProfileDTO dto = profile.convertToDto(profile);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/getProfileWithoutContactByEmail/{email}")
    public ResponseEntity<?> getProfileWithoutContactByEmail(@PathVariable String email){
        Optional<Profile> existingProfile = profileService.getProfileByEmail(email);
        logger.info("email : "+email);
        if(existingProfile.isEmpty()){
            return ResponseEntity.status(HttpStatus.CREATED).body(new ErrorResponse(LocalDateTime.now(),"Register a account with email : "+email,"Profile with email \"+email+\" not found"));
        }
        Profile profile = existingProfile.get();
        ProfileWithoutContact withoutContact = profile.convertToProfileWithoutContact(profile);
        return ResponseEntity.ok(withoutContact);
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDTO profile) {
            // Check if the profile exists in the database by email or phone number
        Optional<Profile> existingProfile = profileService.getProfileByEmail(profile.getEmail());
        if (existingProfile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile with email " + profile.getEmail() + " not found.");
        }
        // Update the existing profile with the new data
        Profile existing = existingProfile.get();
        existing.setName(profile.getName());
        existing.setLocation(profile.getLocation());
        existing.setPhoneNumber(profile.getPhoneNumber());
        existing.setCountryCode(profile.getCountryCode());
        // Save the updated profile to the database
        Profile updatedProfile = profileService.saveProfile(existing);
        return ResponseEntity.ok(updatedProfile.convertToDto(updatedProfile));
    }
    @PostMapping("/registerProfile")
    public ResponseEntity<?> registerProfile(@RequestBody Profile profile){
        Profile newProf = profileService.saveProfile(profile);
        return ResponseEntity.ok(newProf);
    }
    @GetMapping("/getOtp")
    @ResponseBody
    public ResponseEntity<?> getOtp(@ModelAttribute CallerID callerID){
        String mobileNumber = callerID.getCountryCode()+" "+callerID.getNumber();
        String response = otpService.sendToPhone(mobileNumber);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/validate")
    public ResponseEntity<?> validateOtp(@RequestBody ValidateOtpDTO validateOtp){

        return otpService.validateOtp(validateOtp.getMobileNumber(),validateOtp.getOtp(),validateOtp.getEmail());
    }
    @GetMapping("/getProfile")
    @ResponseBody
    public ResponseEntity<?> getProfile(@ModelAttribute CallerID callerId){
        logger.info("In api controller before"+callerId.toString());
        if(callerId.getCountryCode().startsWith("%2B")){
            callerId.setCountryCode(callerId.getCountryCode().replace("%2B","+"));
        }
        logger.info("In api controller after"+callerId.toString());
        Profile profile = profileService.getProfileByCallerID(callerId.getNumber(),callerId.getCountryCode())
                        .orElseThrow(() -> new ProfileNotFoundException("Profile with phone number : "
                                + callerId.getCountryCode() + " " + callerId.getNumber() + " not found."));
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/searchByName/{name}")
    public ResponseEntity<List<Profile>> searchByName(@PathVariable String name){
        List<Profile> listOfPeople = profileService.searchProfilesByName(name);
        return ResponseEntity.ok(listOfPeople);
    }
}
