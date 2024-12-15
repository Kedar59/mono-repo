package com.apiGateway.services;

import com.apiGateway.entities.Profile;
import com.apiGateway.repositories.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService implements UserDetailsService {
//    @Autowired
//    private ProfileRepository repository;
//    @Autowired
//    private PasswordEncoder encoder;
    private final ProfileRepository repository;
    private final PasswordEncoder encoder;

    @Autowired
    public ProfileService(ProfileRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }
    public Profile saveProfile(Profile profile) {
        return repository.save(profile);
    }
    private Logger logger = LoggerFactory.getLogger(ProfileService.class);

    public Optional<Profile> getProfileByPhoneNumber(String phoneNumber){
        return repository.findByPhoneNumber(phoneNumber);
    }


    public Optional<Profile> getProfileByEmail(String email) {
        return repository.findByEmail(email);
    }


    public Optional<Profile> getProfileByCallerID(String number,String countryCode){
        logger.info("in profileServiceImplements number:"+number+" countryCode : "+countryCode);
        return repository.findByCallerId(number,countryCode);
    }


    public List<Profile> searchProfilesByName(String partialName) {
        // This will return an empty list if no matches are found
        return repository.findByNameContaining(partialName);
    }
    public Profile addProfile(Profile profile) {
        // Encode password before saving the user
        profile.setPassword(encoder.encode(profile.getPassword()));
        return repository.save(profile);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Profile> profileDetails = repository.findByEmail(email);
        return profileDetails.map(ProfileDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException(
                "Email not found please register your email : "+email));
    }
}
