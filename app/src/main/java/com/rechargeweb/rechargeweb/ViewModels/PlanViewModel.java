package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.DTH;
import com.rechargeweb.rechargeweb.DthCustomerInfo;
import com.rechargeweb.rechargeweb.Model.FetchOperator;
import com.rechargeweb.rechargeweb.Plans;
import com.rechargeweb.rechargeweb.Repository;
import com.rechargeweb.rechargeweb.Roffer;

import java.util.List;

public class PlanViewModel extends ViewModel {

    Repository repository;

    public PlanViewModel() {

        repository = Repository.getInstance();
    }

    //Get operator and circle id using the user's mobile number
    public LiveData<FetchOperator>getOperatorDetails(String mobileNumber){
        return repository.fectchMobileDetails(mobileNumber);
    }

    //Get Mobile Recharge plans list
    public LiveData<List<Plans>>getMobileRechargePlanList(String circle, String operator, String type){
        return repository.getMobileRechargePlans(circle,operator,type);
    }

    //Get list of special offer for a mobile number
    public LiveData<List<Roffer>>specialOfferList(String mobileNumber, String operator){
        return repository.getSpecialOfferList(mobileNumber, operator);
    }

    //Get list of DTH plans list
    public LiveData<List<DTH>>getDthPlans(String apiKey, String operatorName){
        return repository.getDthPlansList(apiKey, operatorName);
    }

    //Get Dth Customer Information
    public LiveData<DthCustomerInfo>getDthCustomerInfo(String number, String operatorName){
        return repository.getDthCusomerInfo(number, operatorName);
    }
}
