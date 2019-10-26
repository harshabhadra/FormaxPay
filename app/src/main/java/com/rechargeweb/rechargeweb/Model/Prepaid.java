package com.rechargeweb.rechargeweb.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Prepaid implements Parcelable {

    public Prepaid(String image, String id, String operatorName) {
        this.image = image;
        this.id = id;
        this.operatorName = operatorName;
    }

    public Prepaid(String image) {
        this.image = image;
    }

    private String image;
    private String id;
    private String operatorName;

    protected Prepaid(Parcel in) {
        image = in.readString();
        id = in.readString();
        operatorName = in.readString();
    }

    public static final Creator<Prepaid> CREATOR = new Creator<Prepaid>() {
        @Override
        public Prepaid createFromParcel(Parcel in) {
            return new Prepaid(in);
        }

        @Override
        public Prepaid[] newArray(int size) {
            return new Prepaid[size];
        }
    };

    public String getImage() {
        return image;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(id);
        dest.writeString(operatorName);
    }
}
