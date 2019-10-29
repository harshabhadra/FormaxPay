package com.rechargeweb.rechargeweb.Constant;

import com.rechargeweb.rechargeweb.Model.Items;
import com.rechargeweb.rechargeweb.R;

import java.util.ArrayList;
import java.util.List;

public class DummyData {

    public DummyData() {
    }


    //Get Utility service items
    public List<Items> getItemsList() {

        List<Items> dummyList = new ArrayList<>();
        dummyList.add(new Items(R.mipmap.mobile_icon, "Mobile"));
        dummyList.add(new Items(R.mipmap.dth_icon, "DTH"));
        dummyList.add(new Items(R.mipmap.landfone_icon, "Telephone"));
        dummyList.add(new Items(R.mipmap.electricity_icon, "Electricity"));
        dummyList.add(new Items(R.mipmap.broadband_icon, "Broadband"));
        dummyList.add(new Items(R.mipmap.gas_icon, "Gas"));
        dummyList.add(new Items(R.mipmap.life_insurance_icon, "Insurance"));
        dummyList.add(new Items(R.mipmap.pancard_icon,"PAN Coupon"));
        return dummyList;
    }

    //Get report items
    public List<Items> getReportList() {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add(new Items(R.drawable.recharge, "Recharge"));
        itemsList.add(new Items(R.drawable.credit, "Credit"));
        itemsList.add(new Items(R.drawable.debit, "Debit"));
        itemsList.add(new Items(R.drawable.coupon, "Coupon"));
//        itemsList.add(new Items(R.mipmap.money_transfer_report, "Money Transfer"));
//        itemsList.add(new Items(R.mipmap.fund_request_report, "Fund Request"));


//        itemsList.add(new Items(R.mipmap.account_statement_report, "Account Statement"));

        return itemsList;
    }

    //Get balance items
    public List<Items>getMoneyItemsList(){
        List<Items>itemsList = new ArrayList<>();
        itemsList.add(new Items(R.mipmap.check_balancetwo, "Check Balance"));
        itemsList.add(new Items(R.mipmap.passbook_icontwo, "Passbook"));
        itemsList.add(new Items(R.mipmap.add_moneytwo, "Add Money"));
        itemsList.add(new Items(R.mipmap.fund_requesttwo, "Fund Request"));
        return itemsList;
    }

    //Get Payment mode
    public List<String>getPaymentMode(){
        List<String>paymentList = new ArrayList<>();
        paymentList.add("NEFT");
        paymentList.add("IMPS");
        paymentList.add("UPI/BHIM");
        paymentList.add("RTGS");
        paymentList.add("CASH DEPOSIT");
        paymentList.add("BANK TRANSFER");
        return paymentList;
    }

    //Get Banking service item list
    public List<Items>getBankingItems(){
        List<Items>bankingItems = new ArrayList<>();
        bankingItems.add(new Items((R.drawable.dmt),"DMT"));
        bankingItems.add(new Items(R.drawable.aeps_icon,"AEPS"));
        return bankingItems;
    }
}
