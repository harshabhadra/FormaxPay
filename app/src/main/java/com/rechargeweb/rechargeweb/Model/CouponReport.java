package com.rechargeweb.rechargeweb.Model;

public class CouponReport {

    private String psaid;
    private String name;
    private String quantity;
    private String coupon_price;
    private String total_price;
    private String txn_id;
    private String status;
    private String created_on;
    private String remark;

    public CouponReport(String psaid, String name, String quantity, String coupon_price, String total_price, String txn_id, String status, String created_on,String remark) {
        this.psaid = psaid;
        this.name = name;
        this.quantity = quantity;
        this.coupon_price = coupon_price;
        this.total_price = total_price;
        this.txn_id = txn_id;
        this.status = status;
        this.created_on = created_on;
        this.remark = remark;
    }

    public CouponReport(String psaid) {
        this.psaid = psaid;
    }

    public String getPsaid() {
        return psaid;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_on() {
        return created_on;
    }

    public String getRemark() {
        return remark;
    }
}
