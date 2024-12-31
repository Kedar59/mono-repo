package com.truecaller.projections;

public class ValidateOtpDTO {
    private String mobileNumber;
    private String otp;
    private String email;
    public ValidateOtpDTO(){}
    public ValidateOtpDTO(String mobileNumber, String otp,String email) {
        this.mobileNumber = mobileNumber;
        this.otp = otp;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
