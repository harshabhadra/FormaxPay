package com.rechargeweb.rechargeweb;

public class DTH {

    private String amount;
    private String description;

    public DTH(String amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public DTH(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
