package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Model.AepsLogIn;
import com.rechargeweb.rechargeweb.Repository;

public class AepsViewModel extends ViewModel {

    private Repository repository;

    public AepsViewModel() {

        repository = Repository.getInstance();
    }

    //Aeps Log in
    public LiveData<AepsLogIn> aepsLogIn(String session_id, String serviceType, String auth){

        return repository.aepsLogIn(session_id,serviceType,auth);
    }
}
