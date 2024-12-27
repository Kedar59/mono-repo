package com.truecaller.services.impls;

import com.truecaller.entities.Profile;
import com.truecaller.entities.SpamReport;
import com.truecaller.repositories.ProfileRepository;
import com.truecaller.services.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileServiceImplements implements ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Override
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }
    private Logger logger = LoggerFactory.getLogger(ProfileServiceImplements.class);
    @Override
    public Optional<Profile> getProfileByPhoneNumber(String phoneNumber){
        return profileRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<Profile> getProfileByEmail(String email) {
        return profileRepository.findByEmail(email);
    }

    @Override
    public Optional<Profile> getProfileByCallerID(String number,String countryCode){
        logger.info("in profileServiceImplements number:"+number+" countryCode : "+countryCode);
        return profileRepository.findByCallerId(number,countryCode);
    }

    @Override
    public List<Profile> searchProfilesByName(String partialName) {
        // This will return an empty list if no matches are found
        return profileRepository.findByNameContaining(partialName);
    }

    @Override
    public void updateSpamersProfile(Profile spammer, SpamReport spamReport) {
        if(spamReport.isSpamCall()){
            spammer.setNumberOfSpamCallReports(spammer.getNumberOfSpamCallReports()+1);
        } else {
            spammer.setNumberOfSpamSMSReports(spammer.getNumberOfSpamSMSReports() + 1);
        }
        profileRepository.save(spammer);
    }

    @Override
    public void createSpammerProfile(SpamReport spamReport) {
        int numberOfSpamCalls = 0;
        int numberOfSpamSMS = 0;
        if(spamReport.isSpamCall()) {
            numberOfSpamCalls++;
        } else numberOfSpamSMS++;
        Profile spammerProfile = new Profile(spamReport.getSpammerNumber(),spamReport.getSpammerCountryCode(),numberOfSpamCalls,numberOfSpamSMS);
        profileRepository.save(spammerProfile);
    }

    @Override
    public List<Profile> getListOfProfiles(List<String> userIds) {
        List<Profile> listOfMemberProfiles = new ArrayList<Profile>();
        for(String profileId : userIds){
            Optional<Profile> existing = profileRepository.findById(profileId);
            existing.ifPresent(listOfMemberProfiles::add);
        }
        return listOfMemberProfiles;
    }

}
