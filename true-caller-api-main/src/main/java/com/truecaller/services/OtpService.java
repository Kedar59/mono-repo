package com.truecaller.services;

import com.truecaller.config.TwilioConfig;
import com.truecaller.entities.Profile;
import com.truecaller.error.ErrorResponse;
import com.truecaller.projections.User;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    @Autowired
    private TwilioConfig twilioConfig;
    @Autowired
    private ProfileService profileService;
    // This map will store OTPs and their expiration time temporarily in memory
    private ConcurrentHashMap<String, OtpData> otpStorage = new ConcurrentHashMap<>();
    private static final int OTP_EXPIRATION_MINUTES = 5;
    private static final long OTP_EXPIRATION_MILLIS = OTP_EXPIRATION_MINUTES * 60 * 1000;

    //generate OTP
//    public String generateOtp(){
//        int otp = (int)(Math.random()*1000000);
//        String otpS = String.format("%6d",otp);
//        if(otpS.length()==5) otpS+="0";
//        return otpS;
//    }
//

    public String generateOtp(){
        int otp = 100000 + (int) (Math.random() * 900000); // Ensures a random number between 100000 and 999999
        return String.valueOf(otp); // Converts the number directly to a string
    }
    // send otp
    public String sendToTelegram(String mobileNumber){
        String otp = generateOtp();
        long expirationTime = System.currentTimeMillis() + OTP_EXPIRATION_MILLIS;
        OtpData otpData = new OtpData(otp, expirationTime);
        otpStorage.put(mobileNumber, otpData);
        return otp;
    }
    public String sendToPhone(String mobileNumber){
        String otp = generateOtp();
        PhoneNumber reciepientPhoneNumber = new PhoneNumber(mobileNumber);
        PhoneNumber senderPhoneNumber = new PhoneNumber(twilioConfig.getPhoneNumber());
        String messageBody = "your one time password(otp) is : "+otp;
        Message message = Message.creator(reciepientPhoneNumber, senderPhoneNumber, messageBody).create();
        // Store OTP in-memory with expiration time (using epoch time in milliseconds)
        long expirationTime = System.currentTimeMillis() + OTP_EXPIRATION_MILLIS;
        OtpData otpData = new OtpData(otp, expirationTime);
        otpStorage.put(mobileNumber, otpData);
        return "otp sent successfully to " + mobileNumber;
    }
    // validate OTP
    public ResponseEntity<?> validateOtp(String mobileNumber, String otp,String email){
        OtpData otpData = otpStorage.get(mobileNumber);
        ErrorResponse error = new ErrorResponse();
        // Check if OTP exists and hasn't expired
        if (otpData != null && otpData.getOtp().equals(otp)) {
            if (System.currentTimeMillis() < otpData.getExpirationTime()) {
                // OTP is valid
                Optional<Profile> existingProfile = profileService.getProfileByEmail(email);
                if(existingProfile.isEmpty()){
                    error.setDetails("Counld'nt fetch profile with email : "+email);
                    error.setMessage("Error while fetching profile with given email please try again later : ");
                } else {
                    Profile profile = existingProfile.get();
                    profile.setVerified(true);
                    Profile updatedProfile = profileService.saveProfile(profile);
                    return ResponseEntity.ok(new User(updatedProfile.getId(),updatedProfile.getEmail(),updatedProfile.getName(),updatedProfile.isVerified()));
                }
            } else {
                // OTP expired
                otpStorage.remove(mobileNumber); // Clean up expired OTP
                error.setMessage("Otp expires after 5 minuits of receiving it please try again with another otp.");
                error.setDetails("Otp for mobile number "+mobileNumber+" has expired");
            }
        } else {
            error.setMessage("Otp not found please request otp from mobile number of mouthshut account. On telegram.");
            error.setDetails("Otp for mobile number : "+mobileNumber+" not found in memory database.");
        }
        if (otpData != null && !otpData.getOtp().equals(otp)) {
            error.setMessage("Incorrect otp. Please try again");
            error.setMessage("Incorrect otp for mobile number : "+mobileNumber);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(error);
    }
    private static class OtpData {
        private String otp;
        private long expirationTime; // Epoch time in milliseconds

        public OtpData(String otp, long expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public long getExpirationTime() {
            return expirationTime;
        }
    }
}
