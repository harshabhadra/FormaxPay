package com.rechargeweb.rechargeweb.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUp {

    @SerializedName("message")
    @Expose
    private String message;

    public SignUp(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
