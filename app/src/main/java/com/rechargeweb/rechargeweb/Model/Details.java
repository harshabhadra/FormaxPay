package com.rechargeweb.rechargeweb.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("business_name")
    @Expose
    private String business_name;
    @SerializedName("wallet_1")
    @Expose
    private String wallet_1;
    @SerializedName("wallet_2")
    @Expose
    private String wallet_2;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("session_id")
    @Expose
    private String session_id;
    @SerializedName("auth_key")
    @Expose
    private String api_key;
    @SerializedName("user_type")
    @Expose
    private String user_type;

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getWallet_1() {
        return wallet_1;
    }

    public void setWallet_1(String wallet_1) {
        this.wallet_1 = wallet_1;
    }

    public String getWallet_2() {
        return wallet_2;
    }

    public void setWallet_2(String wallet_2) {
        this.wallet_2 = wallet_2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Details{" +
                "message ='" + message + '\'' +
                ",business_name ='" + business_name + '\'' +
                ",wallet_1 ='" + wallet_1 + '\'' +
                ",wallet_2 ='" + wallet_2 + '\'' +
                ",name ='" + name +
                '}';
    }
}
