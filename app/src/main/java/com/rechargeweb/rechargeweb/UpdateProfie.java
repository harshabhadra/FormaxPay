package com.rechargeweb.rechargeweb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfie
{

    @SerializedName("message")
    @Expose
    private String message;

    public UpdateProfie(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
