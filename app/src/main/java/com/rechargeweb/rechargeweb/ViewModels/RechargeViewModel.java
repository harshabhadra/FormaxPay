package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Model.NumberDetect;
import com.rechargeweb.rechargeweb.Model.Recharge;
import com.rechargeweb.rechargeweb.Repository;

public class RechargeViewModel extends ViewModel {

    Repository repository;

    public RechargeViewModel(){

        repository = Repository.getInstance();
    }

    //Get number details
    public LiveData<NumberDetect> getNumberDetails(String token, int number){
        return repository.getNumbeDetails(token,number);
    }


    //get recharge status
    public LiveData<Recharge> getRechargeStatus(String userId, String auth, String number, String operatorId, String amount) {
        return repository.getRechargeStatus(userId, auth, number, operatorId, amount);
    }
}
