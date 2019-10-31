package com.rechargeweb.rechargeweb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AepsReport {

    @SerializedName("txn_amount")
    @Expose
    public String txnAmount;
    @SerializedName("service_type")
    @Expose
     String serviceType;
    @SerializedName("aeps_type")
    @Expose
     String aepsType;
    @SerializedName("order_id")
    @Expose
    String orderId;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("created_on")
    @Expose
    String createdOn;
    @SerializedName("message")
    @Expose
    String message;

    public AepsReport(String txnAmount, String serviceType, String aepsType, String orderId, String status, String createdOn, String message) {
        this.txnAmount = txnAmount;
        this.serviceType = serviceType;
        this.aepsType = aepsType;
        this.orderId = orderId;
        this.status = status;
        this.createdOn = createdOn;
        this.message = message;
    }

    public String getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(String txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getAepsType() {
        return aepsType;
    }

    public void setAepsType(String aepsType) {
        this.aepsType = aepsType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
