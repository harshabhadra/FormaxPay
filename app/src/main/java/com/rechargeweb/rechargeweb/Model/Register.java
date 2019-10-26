package com.rechargeweb.rechargeweb.Model;

public class Register {

    String message;
    String id;
    int is_verfied;

    public Register(String message, String id, int is_verfied) {
        this.message = message;
        this.id = id;
        this.is_verfied = is_verfied;
    }

    public Register(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public int getIs_verfied() {
        return is_verfied;
    }
}
