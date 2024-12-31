package com.apiGateway.controller;
import com.apiGateway.DTO.User;
import com.apiGateway.entities.LoginResponse;
import com.apiGateway.entities.Profile;
import com.apiGateway.errors.ErrorResponse;
import com.apiGateway.services.JwtService;
import com.apiGateway.entities.AuthRequest;
import com.apiGateway.services.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/gateway/auth")
public class AuthController {

    @Autowired
    private ProfileService service;

    @Autowired
    private JwtService jwtService;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public ResponseEntity<?> addNewUser(@RequestBody Profile profile) {
        Optional<Profile> existingUserEmail = service.getProfileByEmail(profile.getEmail());
        Optional<Profile> existingUserNumber = service.getProfileByCallerID(profile.getPhoneNumber(),profile.getCountryCode());
        if(existingUserEmail.isPresent()){
            return ResponseEntity.status(HttpStatus.FOUND).body(new ErrorResponse(
                    "Please register with a new email id.","A user with email "+profile.getEmail()+" already exists."));
        } else if(existingUserNumber.isPresent()){
            return ResponseEntity.status(HttpStatus.FOUND).body(new ErrorResponse(
                    "Please register with a new phone number.",
                    "A user with phone number "+profile.getCountryCode()+" "+profile.getPhoneNumber()+" already exists."));
        }
        return ResponseEntity.ok(service.addProfile(profile));
    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Optional<Profile> existingUserEmail = service.getProfileByEmail(authRequest.getEmail());
        if(existingUserEmail.isEmpty()){
            return ResponseEntity.status(201).body(new ErrorResponse(
                    "Please register with email "+authRequest.getEmail(),
                    "A Profile with email "+authRequest.getEmail()+" doesn't exists."));
        } else {
            Profile profile = existingUserEmail.get();
            if(!service.checkPassword(authRequest.getPassword(),profile.getPassword())){
                return ResponseEntity.status(201).body(new ErrorResponse(
                        "Incorrect password.",
                        "Passwords dont match."));
            }
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            Profile profile = existingUserEmail.get();
            User user = new User(profile.getId(), profile.getEmail(), profile.getName(), profile.isVerified());
            return ResponseEntity.ok(new LoginResponse(jwtService.generateToken(authRequest.getEmail()), user));
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

//    @GetMapping("/user/userProfile")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
//    public String userProfile() {
//        return "Welcome to User Profile";
//    }
//
//    @GetMapping("/admin/adminProfile")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public String adminProfile() {
//        return "Welcome to Admin Profile";
//    }


}