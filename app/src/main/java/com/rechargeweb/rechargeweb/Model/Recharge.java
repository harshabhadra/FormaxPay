package com.rechargeweb.rechargeweb.Model;

public class Recharge {

    private String number, amount, status, operator_name, txn_id, opt_txn_id, created_on, message, logo;

    public Recharge(String number, String amount, String status, String operator_name, String txn_id, String opt_txn_id, String created_on, String message, String logo) {
        this.number = number;
        this.amount = amount;
        this.status = status;
        this.operator_name = operator_name;
        this.txn_id = txn_id;
        this.opt_txn_id = opt_txn_id;
        this.created_on = created_on;
        this.message = message;
        this.logo = logo;
    }

    public Recharge(String number) {
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

    public String getLogo() {
        return logo;
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

    public String getMessage() {
        return message;
    }
}
