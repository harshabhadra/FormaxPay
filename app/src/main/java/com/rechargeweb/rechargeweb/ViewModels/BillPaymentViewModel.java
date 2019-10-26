package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Model.BillPay;
import com.rechargeweb.rechargeweb.Model.ElectricStatus;
import com.rechargeweb.rechargeweb.Repository;

public class BillPaymentViewModel extends ViewModel {

    Repository repository;

    public BillPaymentViewModel() {
        repository = Repository.getInstance();
    }

    //Get status of electric bill
    public LiveData<ElectricStatus> getElectricBillStatus(String auth, String customer_id, String code){
        return repository.getElectricBillStatus(auth,customer_id,code);
    }

    //Get status of electric bill with two parameter
    public LiveData<ElectricStatus>getElectricBillStatusTWo(String auth, String customer_id, String code, String parameter2){
        return repository.getElectricBillStatusWithP(auth,customer_id,code,parameter2);
    }

    //Get electric bill payment details
    public LiveData<BillPay>getElectricbillPaymentDetials(String auth, String session_id, String counsumer_id, String code, int amount, String ref_id){
        return repository.getBillPaymentDetails(auth,session_id,counsumer_id,code,amount,ref_id);
    }
}
