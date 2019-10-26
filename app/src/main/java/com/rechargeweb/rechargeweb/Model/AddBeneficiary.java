package com.rechargeweb.rechargeweb.Model;

public class AddBeneficiary {

    String status;
    String message;

    public AddBeneficiary(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public AddBeneficiary(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
