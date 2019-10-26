package com.rechargeweb.rechargeweb.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PlanDetails implements Parcelable {
    public int id;

    public String amount;
    public String detais;
    public String validity;
    public String talkTime;

    public PlanDetails(String amount, String detais, String validity, String talkTime) {
        this.amount = amount;
        this.detais = detais;
        this.validity = validity;
        this.talkTime = talkTime;
    }
    public PlanDetails(String amount) {
        this.amount = amount;
    }

    protected PlanDetails(Parcel in) {
        id = in.readInt();
        amount = in.readString();
        detais = in.readString();
        validity = in.readString();
        talkTime = in.readString();
    }

    public static final Creator<PlanDetails> CREATOR = new Creator<PlanDetails>() {
        @Override
        public PlanDetails createFromParcel(Parcel in) {
            return new PlanDetails(in);
        }

        @Override
        public PlanDetails[] newArray(int size) {
            return new PlanDetails[size];
        }
    };

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDetais() {
        return detais;
    }

    public void setDetais(String detais) {
        this.detais = detais;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(String talkTime) {
        this.talkTime = talkTime;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(amount);
        dest.writeString(detais);
        dest.writeString(validity);
        dest.writeString(talkTime);
    }
}
