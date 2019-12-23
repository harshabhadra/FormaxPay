package com.rechargeweb.rechargeweb.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchOperator implements Parcelable {

    @SerializedName("ERROR")
    @Expose
    private String eRROR;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("Operator")
    @Expose
    private String operator;
    @SerializedName("Circle")
    @Expose
    private String circle;
    @SerializedName("CircleCode")
    @Expose
    private String circleCode;
    @SerializedName("Message")
    @Expose
    private String message;

    protected FetchOperator(Parcel in) {
        eRROR = in.readString();
        sTATUS = in.readString();
        mobile = in.readString();
        operator = in.readString();
        circle = in.readString();
        circleCode = in.readString();
        message = in.readString();
    }

    public FetchOperator(String mobile) {
        this.mobile = mobile;
    }

    public FetchOperator(String eRROR, String sTATUS, String mobile, String operator, String circle, String circleCode, String message) {
        this.eRROR = eRROR;
        this.sTATUS = sTATUS;
        this.mobile = mobile;
        this.operator = operator;
        this.circle = circle;
        this.circleCode = circleCode;
        this.message = message;
    }

    public static final Creator<FetchOperator> CREATOR = new Creator<FetchOperator>() {
        @Override
        public FetchOperator createFromParcel(Parcel in) {
            return new FetchOperator(in);
        }

        @Override
        public FetchOperator[] newArray(int size) {
            return new FetchOperator[size];
        }
    };

    public String getERROR() {
        return eRROR;
    }

    public void setERROR(String eRROR) {
        this.eRROR = eRROR;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getCircleCode() {
        return circleCode;
    }

    public void setCircleCode(String circleCode) {
        this.circleCode = circleCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eRROR);
        dest.writeString(sTATUS);
        dest.writeString(mobile);
        dest.writeString(operator);
        dest.writeString(circle);
        dest.writeString(circleCode);
        dest.writeString(message);
    }
}
