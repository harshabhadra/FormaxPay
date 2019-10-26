package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Model.Beneficiary;
import com.rechargeweb.rechargeweb.Model.Register;
import com.rechargeweb.rechargeweb.Model.Remitter;
import com.rechargeweb.rechargeweb.Model.Validate;
import com.rechargeweb.rechargeweb.Repository;

import java.util.List;

public class DmtViewModel extends ViewModel {

    Repository repository;

    public DmtViewModel() {

        repository = Repository.getInstance();
    }

    //Get Remitter message
    public LiveData<String> getRemitterMessage(String auth, String mobile){

        return repository.getRemitterMessage(auth,mobile);
    }

    //Get Beneficiary details
    public LiveData<List<Beneficiary>>getBeneficiaryDetails(String auth,String mobile){

        return repository.getBenefiaciaryList(auth,mobile);
    }

    //Get Remitter details
    public LiveData<Remitter>getRemitterDetails(String auth, String mobile){
        return repository.getRemitterDetails(auth,mobile);
    }

    //Get Id
    public LiveData<String>getId(String auth, String mobile){
        return repository.getIdLiveData(auth,mobile);
    }

    //Get register response
    public LiveData<Register>registerUser(String auth,String mobile, String first, String last, String pincode){
        return repository.getRegisterMessage(auth,mobile,first,last,pincode);
    }

    //Validate register
    public LiveData<Validate>validateUser(String auth, String mobile, String remitter_id, String otp){
        return repository.getValidateMessage(auth,mobile,remitter_id,otp);
    }
}
