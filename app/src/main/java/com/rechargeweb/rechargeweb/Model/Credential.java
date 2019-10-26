package com.rechargeweb.rechargeweb.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Credential {

    @SerializedName("psa_id")
    @Expose
    private String psaId;
    @SerializedName("vle_name")
    @Expose
    private String vleName;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("price")
    @Expose
    private String price;

    public Credential(String psaId, String vleName, String message, String status, String remark,String price) {
        this.psaId = psaId;
        this.vleName = vleName;
        this.message = message;
        this.status = status;
        this.remark = remark;
        this.price = price;
    }

    public Credential(String psaId, String vleName, String message) {
        this.psaId = psaId;
        this.vleName = vleName;
        this.message = message;
    }

    public Credential(String message) {
        this.message = message;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPsaId() {
        return psaId;
    }

    public void setPsaId(String psaId) {
        this.psaId = psaId;
    }

    public String getVleName() {
        return vleName;
    }

    public void setVleName(String vleName) {
        this.vleName = vleName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
