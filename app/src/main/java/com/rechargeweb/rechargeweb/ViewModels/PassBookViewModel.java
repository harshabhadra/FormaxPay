package com.rechargeweb.rechargeweb.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rechargeweb.rechargeweb.Model.Passbook;
import com.rechargeweb.rechargeweb.Repository;

import java.util.List;

public class PassBookViewModel extends ViewModel {

    Repository repository;

    public PassBookViewModel() {

        repository = Repository.getInstance();
    }

    //Get Passbook details
    public LiveData<List<Passbook>> getPassBookDetails(String id, String auth) {
        return repository.getPassBookDetails(id, auth);
    }

    //Get Passbook details by date
    public LiveData<List<Passbook>> getPassbookDetailsByDate(String id, String auth, String from, String toDate) {
        return repository.getPassbookdetailsByDate(id, auth, from, toDate);
    }
}
