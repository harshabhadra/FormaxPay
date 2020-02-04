package com.rechargeweb.rechargeweb;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MoveToBankViewModel extends ViewModel {

    private Repository repository;

    public MoveToBankViewModel() {

        repository = Repository.getInstance();
    }

    //Move Money to Bank from Wallet 2
    public LiveData<Settlement>moveMoneyToBank(String session_idm, String auth, String paymentMode, String amount){
        return repository.moveMoneytoBank(session_idm, auth, paymentMode, amount);
    }
}
