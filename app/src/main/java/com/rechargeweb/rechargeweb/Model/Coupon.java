package com.rechargeweb.rechargeweb.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupon {

    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("txn_id")
    @Expose
    String txn_id;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("price")
    @Expose
    String price;
    @SerializedName("total_price")
    @Expose
    String totoal_price;
    @SerializedName("created_on")
    @Expose
    String created_on;

    public Coupon(String message) {
        this.message = message;
    }

    public Coupon(String message, String txn_id, String status, String price, String totoal_price, String created_on) {
        this.message = message;
        this.txn_id = txn_id;
        this.status = status;
        this.price = price;
        this.totoal_price = totoal_price;
        this.created_on = created_on;
    }

    public String getMessage() {
        return message;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public String getStatus() {
        return status;
    }

    public String getPrice() {
        return price;
    }

    public String getTotoal_price() {
        return totoal_price;
    }

    public String getCreated_on() {
        return created_on;
    }
}
