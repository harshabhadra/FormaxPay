package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Repository;

import java.io.File;

import okhttp3.MultipartBody;

public class UploadKycViewModel extends ViewModel {

    Repository repository;

    public UploadKycViewModel() {

        repository = Repository.getInstance();
    }

    //Upload Kyc details
    //Submit Kyc
    public LiveData<String>submitKyc(String session_id, String auth, String name, String shopName, String dob, String email, String address, String pincode,
                                     String state, String mobile, String city, String aadhaarNo, String panNo, File adharFile, File panFile) {

        return repository.submitKyc(session_id,auth,name,shopName,dob,email,address,pincode,state,mobile,city,aadhaarNo,panNo,adharFile,panFile);
    }

    public LiveData<String>getImageUploadResponse(MultipartBody.Part adharPart, MultipartBody.Part panPart){

        return repository.getImageUploadResponse(adharPart,panPart);
    }
}
