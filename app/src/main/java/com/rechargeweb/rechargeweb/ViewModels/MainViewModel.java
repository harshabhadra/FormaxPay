package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Model.Bank;
import com.rechargeweb.rechargeweb.Model.Coupon;
import com.rechargeweb.rechargeweb.Model.Credential;
import com.rechargeweb.rechargeweb.Model.Passbook;
import com.rechargeweb.rechargeweb.Model.PlanDetails;
import com.rechargeweb.rechargeweb.Model.Prepaid;
import com.rechargeweb.rechargeweb.Model.Support;
import com.rechargeweb.rechargeweb.Repository;

import java.util.List;

public class MainViewModel extends ViewModel {

    Repository repository;

    public MainViewModel() {

        repository = Repository.getInstance();
    }

    //Reset Password
    public LiveData<String> getPasswordLiveData(String name) {
        return repository.getPassword(name);
    }

    //get operator list
    public LiveData<List<Prepaid>> getOperators(String type) {
        return repository.getPrepaidOperator(type);
    }

    //Get support Details
    public LiveData<Support> getSupportDetails(String auth) {
        return repository.getSupportDetails(auth);
    }

    //Get list of operators by state
    public LiveData<List<Prepaid>> getOperatorListByState(String operator, String state) {
        return repository.getOperatorByState(operator, state);
    }

    //Get List of bank
    public LiveData<List<Bank>> getBankDetails() {
        return repository.getBankDetialsList();
    }

    //Get fundresponse
    public LiveData<String> getFundResponse(String id, String auth, String amount, String bank, String pMode, String pDate, String tranId, String walltet) {
        return repository.getFundRequestResponse(id, auth, amount, bank, pMode, pDate, tranId, walltet);
    }

    //Get Credential details
    public LiveData<Credential> getCredentialDetails(String id, String auth) {

        return repository.getCredentialDetails(id, auth);
    }

    //Get Psa registration response
    public LiveData<String> getPsaRegResponse(String id, String auth, String name, String shop, String location, String pincode, String state, String mobile, String email, String pan) {
        return repository.getPsaRegResponse(id, auth, name, shop, location, pincode, state, mobile, email, pan);
    }

    //Get Coupon purchase details
    public LiveData<Coupon> getCouponPurchaseDetails(String auth, String id, String name, String quantity) {
        return repository.viewCouponPurchaseDetails(auth, id, name, quantity);
    }

    //Get list of recharge plans
    public LiveData<List<PlanDetails>> getRechargePlans(String token, String type, String circleId, String optId) {
        return repository.getPlanDetails(token, type, circleId, optId);
    }

    //Get Response message after sending transaction report
    public LiveData<String>sendTranReport(String session_id, String authKey, String mmp_txn, String mer_txn, String amount, String prob, String date, String bank_txn,
                                          String f_code, String clientCode, String bank_name, String authCode, String ipg_txn_id, String merchant_id, String desc,
                                          String discriminator, String udf9, String surcharge, String cardNumber, String udf1, String udf2, String udf3, String udf4, String udf5,
                                          String signature){
        return repository.sendTranReport(session_id,authKey,mmp_txn,mer_txn,amount,prob,date,bank_txn,f_code,clientCode,bank_name,
                authCode,ipg_txn_id,merchant_id,desc,discriminator,udf9,surcharge,cardNumber,udf1,udf2, udf3, udf4, udf5, signature);
    }
}
