package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Repository;

public class ProfileViewModel extends ViewModel {

     Repository repository;

    public ProfileViewModel() {

        repository = Repository.getInstance();
    }

    //Update user profile
    public LiveData<String>updateUserProfile(String session_id, String businessName, String name, String address, String state, String location, String pincode,
                                              String authKey, String panNo, String gstNo, String aadharNo){
        return repository.updateProfile(session_id, businessName,name,address,state,location,pincode,authKey,panNo,gstNo,aadharNo);
    }
}
