package com.rechargeweb.rechargeweb.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {

    private int imageUrl;
    private String name;

    public Items(int imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    protected Items(Parcel in) {
        imageUrl = in.readInt();
        name = in.readString();
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageUrl);
        dest.writeString(name);
    }
}
