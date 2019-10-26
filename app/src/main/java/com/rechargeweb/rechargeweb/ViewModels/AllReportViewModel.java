package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Model.CouponReport;
import com.rechargeweb.rechargeweb.Model.Passbook;
import com.rechargeweb.rechargeweb.Model.RechargeDetails;
import com.rechargeweb.rechargeweb.Repository;

import java.util.List;

public class AllReportViewModel extends ViewModel {

    Repository repository;

    public AllReportViewModel(){

        repository = Repository.getInstance();
    }

    //Get list of recharges
    public LiveData<List<RechargeDetails>> getRechargeList(String session_id, String auth) {
        return repository.getRechargeList(session_id, auth);
    }

    //Get list of recharges by date
    public LiveData<List<RechargeDetails>>getRechargeListByDate(String id, String auth, String from, String to){
        return repository.getRechargeStatusByDate(id,auth,from, to);
    }

    //Get list of credit details
    public LiveData<List<Passbook>>getCreditList(String id, String auth){
        return repository.getCreditSummaryList(id,auth);
    }


    //Get List of credit details by date
    public LiveData<List<Passbook>>getCreditListByDate(String id, String auth, String from, String to){
        return repository.getCreditListByDate(id,auth,from,to);
    }


    //Get list of debit summary
    public LiveData<List<Passbook>>getDebitList(String id, String auth){
        return repository.getDebitSummaryList(id,auth);
    }

    //Get list of debit summary by date
    public LiveData<List<Passbook>>getDebitListByDate(String id,String auth, String from, String to){
        return repository.getDebitListByDate(id,auth,from,to);
    }

    //Get list of coupon report
    public LiveData<List<CouponReport>>getCouponReportList(String id, String auth){

        return repository.getCouponReportList(id,auth);
    }

    //Get list of coupon report by date
    public LiveData<List<CouponReport>>getCouponReportListByDate(String id,String auth, String from, String to){

        return repository.getCouponReportByDate(id, auth, from, to);
    }
}
