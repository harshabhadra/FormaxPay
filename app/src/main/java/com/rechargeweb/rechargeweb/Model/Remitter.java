package com.rechargeweb.rechargeweb.Model;

public class Remitter {

    private String beneficiary_id,name, mobile, address, pincode, city, state, kyc_status,kyc_docs;
    private int consumed_limit,remaining_limit,is_verified,per_txn_limit;

    public Remitter(String beneficiary_id, String name, String mobile, String address,
                    String pincode, String city, String state, String kyc_status,int consumed_limit,
                    int remaining_limit,String kyc_docs, int is_verified, int per_txn_limit) {
        this.beneficiary_id = beneficiary_id;
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.kyc_status = kyc_status;
        this.consumed_limit = consumed_limit;
        this.remaining_limit = remaining_limit;
        this.kyc_docs = kyc_docs;
        this.is_verified = is_verified;
        this.per_txn_limit = per_txn_limit;
    }

    public String getBeneficiary_id() {
        return beneficiary_id;
    }

    public void setBeneficiary_id(String beneficiary_id) {
        this.beneficiary_id = beneficiary_id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getKyc_status() {
        return kyc_status;
    }

    public void setKyc_status(String kyc_status) {
        this.kyc_status = kyc_status;
    }

    public String getKyc_docs() {
        return kyc_docs;
    }

    public void setKyc_docs(String kyc_docs) {
        this.kyc_docs = kyc_docs;
    }

    public int getConsumed_limit() {
        return consumed_limit;
    }

    public void setConsumed_limit(int consumed_limit) {
        this.consumed_limit = consumed_limit;
    }

    public int getRemaining_limit() {
        return remaining_limit;
    }

    public void setRemaining_limit(int remaining_limit) {
        this.remaining_limit = remaining_limit;
    }

    public int getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(int is_verified) {
        this.is_verified = is_verified;
    }

    public int getPer_txn_limit() {
        return per_txn_limit;
    }

    public void setPer_txn_limit(int per_txn_limit) {
        this.per_txn_limit = per_txn_limit;
    }
}
