package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Repository;

import okhttp3.RequestBody;

public class UploadKycViewModel extends ViewModel {

    Repository repository;

    public UploadKycViewModel() {

        repository = Repository.getInstance();
    }

    //Upload Kyc details
    //Submit Kyc
    public LiveData<String> submitKyc(String session_id, String auth, String name, String shopName, String dob, String email, String address, String pincode,
                                      String state, String mobile, String city, String aadhaarNo, String panNo, RequestBody aadharImageurl, RequestBody panImageUrl) {

        return repository.submitKyc(session_id, auth, name, shopName, dob, email, address, pincode, state, mobile, city, aadhaarNo, panNo, aadharImageurl, panImageUrl);
    }
}
