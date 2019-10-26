package com.rechargeweb.rechargeweb.Model;

public class Support {

    private String company;
    private String mobileOne;
    private String mobileTwo;
    private String mobileThree;
    private String email;
    private String emailTwo;
    private String billEmail;
    private String address;
    private String website;
    private String aboutUd;

    public Support(String company, String mobileOne, String mobileTwo, String mobileThree, String email, String emailTwo, String billEmail, String address, String website, String aboutUd) {
        this.company = company;
        this.mobileOne = mobileOne;
        this.mobileTwo = mobileTwo;
        this.mobileThree = mobileThree;
        this.email = email;
        this.emailTwo = emailTwo;
        this.billEmail = billEmail;
        this.address = address;
        this.website = website;
        this.aboutUd = aboutUd;
    }

    public Support(String company) {
        this.company = company;
    }

    public Support(String company, String mobileOne, String mobileTwo, String email, String address, String website) {
        this.company = company;
        this.mobileOne = mobileOne;
        this.mobileTwo = mobileTwo;
        this.email = email;
        this.address = address;
        this.website = website;
    }

    public String getCompany() {
        return company;
    }

    public String getMobileOne() {
        return mobileOne;
    }

    public String getMobileTwo() {
        return mobileTwo;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getMobileThree() {
        return mobileThree;
    }

    public String getEmailTwo() {
        return emailTwo;
    }

    public String getAboutUd() {
        return aboutUd;
    }

    public String getBillEmail() {
        return billEmail;
    }
}
