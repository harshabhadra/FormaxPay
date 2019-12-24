package com.rechargeweb.rechargeweb;

import android.os.Parcel;
import android.os.Parcelable;

public class DthCustomerInfo implements Parcelable {

    private String balance;
    private String customerName;
    private String nextRechargeDate;
    private String address;
    private String planName;
    private String monthlyRecharge;

    public DthCustomerInfo(String balance, String customerName, String nextRechargeDate, String address, String planName, String monthlyRecharge) {
        this.balance = balance;
        this.customerName = customerName;
        this.nextRechargeDate = nextRechargeDate;
        this.address = address;
        this.planName = planName;
        this.monthlyRecharge = monthlyRecharge;
    }

    public DthCustomerInfo(String balance) {
        this.balance = balance;
    }

    protected DthCustomerInfo(Parcel in) {
        balance = in.readString();
        customerName = in.readString();
        nextRechargeDate = in.readString();
        address = in.readString();
        planName = in.readString();
        monthlyRecharge = in.readString();
    }

    public static final Creator<DthCustomerInfo> CREATOR = new Creator<DthCustomerInfo>() {
        @Override
        public DthCustomerInfo createFromParcel(Parcel in) {
            return new DthCustomerInfo(in);
        }

        @Override
        public DthCustomerInfo[] newArray(int size) {
            return new DthCustomerInfo[size];
        }
    };

    public String getBalance() {
        return balance;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getNextRechargeDate() {
        return nextRechargeDate;
    }

    public String getAddress() {
        return address;
    }

    public String getPlanName() {
        return planName;
    }

    public String getMonthlyRecharge() {
        return monthlyRecharge;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(balance);
        dest.writeString(customerName);
        dest.writeString(nextRechargeDate);
        dest.writeString(address);
        dest.writeString(planName);
        dest.writeString(monthlyRecharge);
    }
}
