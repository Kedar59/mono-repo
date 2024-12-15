package com.truecaller.controllers;

import com.truecaller.entities.Profile;
import com.truecaller.exceptions.ProfileNotFoundException;
import com.truecaller.projections.AuthenticationRequest;
import com.truecaller.projections.AuthenticationResponse;
import com.truecaller.projections.CallerID;
import com.truecaller.projections.ValidateOtpDTO;
import com.truecaller.services.OtpService;
import com.truecaller.services.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/truecaller_api/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OtpService otpService;
    private Logger logger = LoggerFactory.getLogger(ProfileController.class);
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticateUser(@RequestBody AuthenticationRequest request) {
        Optional<Profile> profileOptional = profileService.getProfileByEmail(request.getEmail());

        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();

            // Use BCrypt to check password
            if (passwordEncoder.matches(request.getPassword(), profile.getPassword())) {
                return new AuthenticationResponse(
                        null,  // No token generated here
                        true,
                        "Authentication successful",
                        profile

                );
            }
        }

        return new AuthenticationResponse(
                null,
                false,
                "Invalid credentials"
        );
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
        String response = otpService.validateOtp(validateOtp.getMobileNumber(),validateOtp.getOtp()) ?
                "OTP verified" : "OTP incorrect";
        return ResponseEntity.ok(response);
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
