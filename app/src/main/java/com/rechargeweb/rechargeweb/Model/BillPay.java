package com.rechargeweb.rechargeweb.Model;

public class BillPay {
    private String number;
    private String amount;
    private String status;
    private String operatorName;
    private String txnId;
    private String optTxnId;
    private String createdOn;
    private String response;

    public BillPay(String number) {
        this.number = number;
    }

    public BillPay(String number, String amount, String status, String operatorName, String txnId, String optTxnId, String createdOn, String response) {
        this.number = number;
        this.amount = amount;
        this.status = status;
        this.operatorName = operatorName;
        this.txnId = txnId;
        this.optTxnId = optTxnId;
        this.createdOn = createdOn;
        this.response = response;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getOptTxnId() {
        return optTxnId;
    }

    public void setOptTxnId(String optTxnId) {
        this.optTxnId = optTxnId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
