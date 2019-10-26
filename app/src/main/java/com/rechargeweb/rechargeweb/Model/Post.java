package com.rechargeweb.rechargeweb.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("session_id")
    @Expose
    private String session_id;
    @SerializedName("login_id")
    @Expose
    private String login_id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("password")
    @Expose
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    @NonNull
    @Override
    public String toString() {

        return  "Post{" +
                "session_id ='" + session_id + '\'' +
                ", login_id='" + login_id +'\''+
    ",message='" + message+
    '}';
    }
}
