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
import androidx.lifecycle.ViewModelProviders;

import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Profile;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.ProfileViewModel;
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
    private boolean isEdit;
    private String businessName, name, address, state, location, pincode,panNo, gstNo, aadharNo;
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private ApiService apiService;

    private AlertDialog loadingDialog;
    private FragmentProfileBinding fragmentProfileBinding;

    private ProfileViewModel profileViewModel;

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

        //Initializing ProfileViewModle class
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        View view = fragmentProfileBinding.getRoot();
        Log.e(TAG, "Profile Fragment");

        //Initializing ApiServices
        apiService = ApiUtills.getApiService();

        //Initializing Auth key
        auth = getResources().getString(R.string.auth_key);

        //Getting session id and user_id
        HomeActivity activity = (HomeActivity) getActivity();
        id = activity.getSession_id();
        user_id = activity.getUser_id().toUpperCase().trim();

        loadingDialog = createLoadingDialog(getContext());
        loadingDialog.show();
        //Getting Profile Details
        getProfileDetails(id, auth);

        //Initializing isEdit
        isEdit = true;

        //Set on Click listener to fab button
        fragmentProfileBinding.profileEditFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEdit) {
                    fragmentProfileBinding.profilePanInput.setEnabled(true);
                    fragmentProfileBinding.profileGstTextInput.setEnabled(true);
                    fragmentProfileBinding.profileAadharNumberTextInput.setEnabled(true);
                    fragmentProfileBinding.profileLocationTextInput.setEnabled(true);
                    fragmentProfileBinding.profileAddressTextInput.setEnabled(true);
                    fragmentProfileBinding.profilePincodeTextInput.setEnabled(true);
                    fragmentProfileBinding.profileStateTextInput.setEnabled(true);
                    fragmentProfileBinding.profileEditFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_white_24dp));
                    isEdit = false;
                }else {
                    loadingDialog = createLoadingDialog(getContext());
                    loadingDialog.show();
                    fragmentProfileBinding.profilePanInput.setEnabled(false);
                    fragmentProfileBinding.profileGstTextInput.setEnabled(false);
                    fragmentProfileBinding.profileAadharNumberTextInput.setEnabled(false);
                    fragmentProfileBinding.profileLocationTextInput.setEnabled(false);
                    fragmentProfileBinding.profileAddressTextInput.setEnabled(false);
                    fragmentProfileBinding.profilePincodeTextInput.setEnabled(false);
                    fragmentProfileBinding.profileStateTextInput.setEnabled(false);

                    //getting user information
                    businessName = fragmentProfileBinding.profileUserNameTv.getText().toString();
                    address = fragmentProfileBinding.profileAddressTextInput.getText().toString();
                    state = fragmentProfileBinding.profileStateTextInput.getText().toString();
                    location = fragmentProfileBinding.profileLocationTextInput.getText().toString();
                    pincode = fragmentProfileBinding.profilePincodeTextInput.getText().toString().trim();
                    panNo = fragmentProfileBinding.profilePanInput.getText().toString().trim();
                    gstNo = fragmentProfileBinding.profileGstTextInput.getText().toString().trim();
                    aadharNo = fragmentProfileBinding.profileAadharNumberTextInput.getText().toString().trim();

                    //Update user profile
                    updateProfile(id,businessName,name,address,state,location,pincode,auth,panNo,gstNo,aadharNo);

                    fragmentProfileBinding.profileEditFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_white24dp));
                    isEdit = true;
                }
            }
        });

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
                            Log.e(TAG,"Profile is null");
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

    //Update user profile
    private void updateProfile(String session_id, String businessName, String name, String address, String state, String location, String pincode,
                               String authKey, String panNo, String gstNo, String aadharNo){

        profileViewModel.updateUserProfile(session_id, businessName,name,address,state,location,pincode,authKey,panNo,gstNo,aadharNo).observe(this, new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(String s) {
                getProfileDetails(id,authKey);
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
