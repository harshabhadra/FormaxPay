package com.rechargeweb.rechargeweb.Model;

public class RechargeDetails {

    private String number, amount, status, operator_name, txn_id, opt_txn_id, created_on, logo,closing_balance, recharge_by, api_response;

    public RechargeDetails(String number, String amount, String status, String operator_name, String txn_id, String opt_txn_id, String created_on, String logo, String closing_balance, String recharge_by, String api_response) {
        this.number = number;
        this.amount = amount;
        this.status = status;
        this.operator_name = operator_name;
        this.txn_id = txn_id;
        this.opt_txn_id = opt_txn_id;
        this.created_on = created_on;
        this.logo = logo;
        this.closing_balance = closing_balance;
        this.recharge_by = recharge_by;
        this.api_response = api_response;
    }

    public RechargeDetails(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public String getOpt_txn_id() {
        return opt_txn_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public String getLogo() {
        return logo;
    }

    public String getClosing_balance() {
        return closing_balance;
    }

    public String getRecharge_by() {
        return recharge_by;
    }

    public String getApi_response() {
        return api_response;
    }
}
