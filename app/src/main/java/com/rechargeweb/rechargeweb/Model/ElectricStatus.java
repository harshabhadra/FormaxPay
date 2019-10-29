package com.rechargeweb.rechargeweb.Model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ElectricStatus {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("bill_number")
    @Expose
    private String billNumber;
    @SerializedName("bill_date")
    @Expose
    private String billDate;
    @SerializedName("bill_due_date")
    @Expose
    private String billDueDate;
    @SerializedName("bill_period")
    @Expose
    private String billPeriod;
    @SerializedName("bill_amount")
    @Expose
    private Integer billAmount;
    @SerializedName("ref_id")
    @Expose
    private String refId;
    @SerializedName("message")
    @Expose
    private String message;

    public ElectricStatus(String status) {
        this.status = status;
    }

    public ElectricStatus(String status, String customerId, String customerName, String billNumber, String billDate, String billDueDate, String billPeriod, Integer billAmount, String refId, String message) {
        this.status = status;
        this.customerId = customerId;
        this.customerName = customerName;
        this.billNumber = billNumber;
        this.billDate = billDate;
        this.billDueDate = billDueDate;
        this.billPeriod = billPeriod;
        this.billAmount = billAmount;
        this.refId = refId;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillDueDate() {
        return billDueDate;
    }

    public void setBillDueDate(String billDueDate) {
        this.billDueDate = billDueDate;
    }

    public String getBillPeriod() {
        return billPeriod;
    }

    public void setBillPeriod(String billPeriod) {
        this.billPeriod = billPeriod;
    }

    public Integer getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Integer billAmount) {
        this.billAmount = billAmount;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public String toString() {

        return "Electric Status{ "+
                "status='"+status+'\''+
                ", consumer id'" + customerId + '\''+
                ", customerName'"+customerName +'}';
    }
}
