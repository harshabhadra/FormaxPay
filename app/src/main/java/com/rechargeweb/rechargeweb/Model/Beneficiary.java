package com.rechargeweb.rechargeweb.Model;

public class Beneficiary {

    private String id;
    private String name;
    private String mobile;
    private String account;
    private String bank;
    private String status;
    private String last_success_date;
    private String lastSuccessName;
    private String Last_success_imp;
    private String ifsc;
    private String imps;

    public Beneficiary(String id, String name, String mobile, String account, String bank,
                       String status, String last_success_date, String lastSuccessName,
                       String last_success_imp, String ifsc, String imps) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.account = account;
        this.bank = bank;
        this.status = status;
        this.last_success_date = last_success_date;
        this.lastSuccessName = lastSuccessName;
        Last_success_imp = last_success_imp;
        this.ifsc = ifsc;
        this.imps = imps;
    }

    public Beneficiary(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLast_success_date() {
        return last_success_date;
    }

    public void setLast_success_date(String last_success_date) {
        this.last_success_date = last_success_date;
    }

    public String getLastSuccessName() {
        return lastSuccessName;
    }

    public void setLastSuccessName(String lastSuccessName) {
        this.lastSuccessName = lastSuccessName;
    }

    public String getLast_success_imp() {
        return Last_success_imp;
    }

    public void setLast_success_imp(String last_success_imp) {
        Last_success_imp = last_success_imp;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getImps() {
        return imps;
    }

    public void setImps(String imps) {
        this.imps = imps;
    }
}
