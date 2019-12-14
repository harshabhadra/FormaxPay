package com.rechargeweb.rechargeweb.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Otp {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("otp")
    @Expose
    private String otp;

    public Otp(String message, String otp) {
        this.message = message;
        this.otp = otp;
    }

    public Otp(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
