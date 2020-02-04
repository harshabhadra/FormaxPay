package com.rechargeweb.rechargeweb;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile implements Parcelable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("wallet_1")
    @Expose
    private String wallet1;
    @SerializedName("wallet_2")
    @Expose
    private String wallet2;
    @SerializedName("user_type")
    @Expose
    private String user_type;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pan_no")
    @Expose
    private String pan_no;
    @SerializedName("gstin")
    @Expose
    private String gst_no;
    @SerializedName("State")
    @Expose
    private String state;


    protected Profile(Parcel in) {
        message = in.readString();
        businessName = in.readString();
        name = in.readString();
        mobile = in.readString();
        address = in.readString();
        location = in.readString();
        pincode = in.readString();
        createdOn = in.readString();
        wallet1 = in.readString();
        wallet2 = in.readString();
        user_type=in.readString();
        status=in.readString();
        pan_no=in.readString();
        gst_no=in.readString();
        state =in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getWallet1() {
        return wallet1;
    }

    public void setWallet1(String wallet1) {
        this.wallet1 = wallet1;
    }

    public String getWallet2() {
        return wallet2;
    }

    public void setWallet2(String wallet2) {
        this.wallet2 = wallet2;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getStatus() {
        return status;
    }

    public String getPan_no() {
        return pan_no;
    }

    public String getGst_no() {
        return gst_no;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(businessName);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeString(address);
        dest.writeString(location);
        dest.writeString(pincode);
        dest.writeString(createdOn);
        dest.writeString(wallet1);
        dest.writeString(wallet2);
        dest.writeString(user_type);
        dest.writeString(status);
        dest.writeString(pan_no);
        dest.writeString(gst_no);
        dest.writeString(state);
    }
}
