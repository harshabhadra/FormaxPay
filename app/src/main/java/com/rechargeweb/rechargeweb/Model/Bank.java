package com.rechargeweb.rechargeweb.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Bank implements Parcelable {

    private String accountHolder, bankName, accountNumber, ifscCode, status;

    public Bank(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public Bank(String accountHolder, String bankName, String accountNumber, String ifscCode, String status) {
        this.accountHolder = accountHolder;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.status = status;
    }

    protected Bank(Parcel in) {
        accountHolder = in.readString();
        bankName = in.readString();
        accountNumber = in.readString();
        ifscCode = in.readString();
        status = in.readString();
    }

    public static final Creator<Bank> CREATOR = new Creator<Bank>() {
        @Override
        public Bank createFromParcel(Parcel in) {
            return new Bank(in);
        }

        @Override
        public Bank[] newArray(int size) {
            return new Bank[size];
        }
    };

    public String getAccountHolder() {
        return accountHolder;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountHolder);
        dest.writeString(bankName);
        dest.writeString(accountNumber);
        dest.writeString(ifscCode);
        dest.writeString(status);
    }
}
