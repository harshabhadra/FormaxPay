package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.ForgetPassword;
import com.rechargeweb.rechargeweb.Repository;

public class ForgetPassowordViewModel extends ViewModel {

    private Repository repository;

    public ForgetPassowordViewModel() {

        repository = Repository.getInstance();
    }

    //Forget Password
    public LiveData<ForgetPassword>getOtpForgetPassword(String authKey, String mobile){
        return repository.getOtpForPassword(authKey, mobile);
    }
}
