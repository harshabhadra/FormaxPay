package com.rechargeweb.rechargeweb.Model;

public class Validate {

    private String message;
    private int isVerified;

    public Validate(String message, int isVerified) {
        this.message = message;
        this.isVerified = isVerified;
    }

    public Validate(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getIsVerified() {
        return isVerified;
    }
}
