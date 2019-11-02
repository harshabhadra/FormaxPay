package com.rechargeweb.rechargeweb.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class AepsLogIn implements Parcelable {

    private String agentId;
    private String message;
    private String status;
    private String remark;

    public AepsLogIn(String agentId, String message, String status, String remark) {
        this.agentId = agentId;
        this.message = message;
        this.status = status;
        this.remark = remark;
    }

    public AepsLogIn(String agentId) {
        this.agentId = agentId;
    }

    protected AepsLogIn(Parcel in) {
        agentId = in.readString();
        message = in.readString();
        status = in.readString();
        remark = in.readString();
    }

    public static final Creator<AepsLogIn> CREATOR = new Creator<AepsLogIn>() {
        @Override
        public AepsLogIn createFromParcel(Parcel in) {
            return new AepsLogIn(in);
        }

        @Override
        public AepsLogIn[] newArray(int size) {
            return new AepsLogIn[size];
        }
    };

    public String getAgentId() {
        return agentId;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(agentId);
        dest.writeString(message);
        dest.writeString(status);
        dest.writeString(remark);
    }
}
