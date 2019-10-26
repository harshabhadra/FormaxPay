package com.rechargeweb.rechargeweb.Model;

public class Passbook {

    private String credit_amount, debit_amount, closing_balalnce,opening_balance,transaction_id,narration,wallet_type,created_on;


    public Passbook(String credit_amount) {
        this.credit_amount = credit_amount;
    }

    public Passbook(String credit_amount, String debit_amount, String closing_balalnce, String opening_balance, String transaction_id, String narration, String wallet_type, String created_on) {
        this.credit_amount = credit_amount;
        this.debit_amount = debit_amount;
        this.closing_balalnce = closing_balalnce;
        this.opening_balance = opening_balance;
        this.transaction_id = transaction_id;
        this.narration = narration;
        this.wallet_type = wallet_type;
        this.created_on = created_on;
    }

    public String getCredit_amount() {
        return credit_amount;
    }

    public String getDebit_amount() {
        return debit_amount;
    }

    public String getClosing_balalnce() {
        return closing_balalnce;
    }

    public String getOpening_balance() {
        return opening_balance;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getNarration() {
        return narration;
    }

    public String getWallet_type() {
        return wallet_type;
    }

    public String getCreated_on() {
        return created_on;
    }
}
