package com.rechargeweb.rechargeweb.Ui;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Profile;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.databinding.FragmentProfileBinding;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private String id, user_id;
    private String auth;
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private ApiService apiService;

    private AlertDialog loadingDialog;
    FragmentProfileBinding fragmentProfileBinding;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProfileBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);

        View view = fragmentProfileBinding.getRoot();
        Log.e(TAG, "Profile Fragment");

        apiService = ApiUtills.getApiService();

        auth = getResources().getString(R.string.auth_key);
        HomeActivity activity = (HomeActivity) getActivity();
        id = activity.getSession_id();
        user_id = activity.getUser_id().toUpperCase().trim();

        loadingDialog = createLoadingDialog(getContext());
        //Getting Profile Details
        getProfileDetails(id, auth);

        return view;
    }

    private void getProfileDetails(String id, String key) {

        apiService.getProfileDetails(id, key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Profile>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Profile profile) {

                        loadingDialog.dismiss();
                        if (profile != null) {
                            fragmentProfileBinding.setProfile(profile);
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        Toast.makeText(getContext(),"Failed to Fetch Account Details, Try again Later",Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Profile error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                        Log.e(TAG, "Profile Completed");
                    }
                });
    }

    //Create Loading Dialog
    private AlertDialog createLoadingDialog(Context context){

        View layout = getLayoutInflater().inflate(R.layout.loading_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setCancelable(false);
        return builder.create();
    }
}
