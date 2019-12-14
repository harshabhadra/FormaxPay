package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Model.Otp;
import com.rechargeweb.rechargeweb.Repository;

public class SignUpViewModel extends ViewModel {

    Repository repository;
    public SignUpViewModel() {

        repository = Repository.getInstance();
    }

    //Get Otp details from server
    public LiveData<Otp>getOtp(String authKey, String mobile, String email){

        return repository.getOtpDetails(authKey,mobile,email);
    }

    //Sign Up user
    public LiveData<String>signUpUser(String shopName, String userName, String email, String mobile,String password){
        return repository.signUpUser(shopName,userName,email,mobile,password);
    }
}
