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
        dummyList.add(new Items(R.drawable.mobile_icon, "Mobile"));
        dummyList.add(new Items(R.drawable.dth_icon, "DTH"));
        dummyList.add(new Items(R.drawable.landline_icon, "Telephone"));
        dummyList.add(new Items(R.drawable.electricity_icon, "Electricity"));
        dummyList.add(new Items(R.drawable.boardband_icon, "Broadband"));
        dummyList.add(new Items(R.drawable.pan_icon,"PAN Coupon"));
        dummyList.add(new Items(R.drawable.dmt_icon,"DMT"));
        dummyList.add(new Items(R.drawable.aeps_icon,"YBL AEPS"));
        return dummyList;
    }

    //Get report items
    public List<Items> getReportList() {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add(new Items(R.drawable.mobile_icon, "Recharge"));
        itemsList.add(new Items(R.drawable.aeps_icon,"AEPS"));
        itemsList.add(new Items(R.drawable.coupon, "Coupon"));
        itemsList.add(new Items(R.drawable.credit, "Credit"));
        itemsList.add(new Items(R.drawable.debit, "Debit"));

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
}
