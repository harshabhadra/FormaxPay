package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Model.AddBeneficiary;
import com.rechargeweb.rechargeweb.Repository;

public class BeneficiaryViewModel extends ViewModel {

    Repository repository;

    public BeneficiaryViewModel() {

        repository = Repository.getInstance();
    }

    //Add beneficiary
    public LiveData<AddBeneficiary> addBeneficiary(String auth, String mobile, String remitter_id, String name, String ifscCode, String account){

        return repository.getAddBeneficiaryMessage(auth,mobile,remitter_id,name,ifscCode,account);
    }

    //Validate account
    public LiveData<AddBeneficiary>validateAccount(String session_id, String auth, String account, String ifsc, String mobile){
        return repository.validateAccount(session_id,auth,account,ifsc,mobile);
    }

    //Delete beneficiary
    public LiveData<AddBeneficiary>deleteBeneficiary(String auth,String benId, String remId){
        return  repository.deleteBeneficiary(auth,benId,remId);
    }

    //Delete beneficiary Validation
    public LiveData<AddBeneficiary>deleteBenValidation(String auth, String benId, String remId, String otp){
        return repository.deleteBenValidate(auth,benId,remId,otp);
    }

    public LiveData<String>transferMoney(String session_id, String auth, String mobile, String remitter_id,String name, String ifsc, String account,String ben_id, String amount){

        return repository.transferMoney(session_id,auth,mobile,remitter_id,name,ifsc,account,ben_id,amount);
    }
}
