package com.rechargeweb.rechargeweb.BottomSheetFrag;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Model.Otp;
import com.rechargeweb.rechargeweb.Model.Post;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.SignUpViewModel;
import com.rechargeweb.rechargeweb.databinding.EnterOtpLayoutBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtpFragment extends BottomSheetDialogFragment {

    private static final String TAG = OtpFragment.class.getSimpleName();
    EnterOtpLayoutBinding otpLayoutBinding;
    private String authKey, mobile, email, password, shopName, userName;
    private SignUpViewModel signUpViewModel;

    private String mOtp;
    private String userOtp;
    private int resend = 0;

    private ApiService apiService;

    public OtpFragment() {
        // Required empty public constructor
    }

    public OtpFragment(String authKey, String mobile, String email,String password,String shopName, String userName) {
        this.authKey = authKey;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.shopName = shopName;
        this.userName = userName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initializing DataBinding
        otpLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.enter_otp_layout, container, false);

        //Initializing SignUpViewModel
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        //Initializing ApiServices
        apiService = ApiUtills.getApiService();

        //Getting Otp
        getOtp(authKey,mobile,email);

        return otpLayoutBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Set Text Change Listener to Otp text input layout
        otpLayoutBinding.signUpOtpInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                otpLayoutBinding.signUpOtpLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                otpLayoutBinding.signUpOtpLayout.setErrorEnabled(true);
                if (s.length()<6){
                    otpLayoutBinding.otpVerifyButton.setEnabled(false);
                }else {
                    otpLayoutBinding.otpVerifyButton.setEnabled(true);
                }
            }
        });

        //Set on Click listener to verify otp button
        otpLayoutBinding.otpVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userOtp = otpLayoutBinding.signUpOtpInputText.getText().toString().trim();
                if (userOtp.equals(mOtp)){

                    Toast.makeText(getContext(),"Your Mobile Number is Verified",Toast.LENGTH_LONG).show();
                    signUpUser(shopName,userName,email,mobile,password);
                    otpLayoutBinding.otpLayoutGroup.setVisibility(View.INVISIBLE);
                    otpLayoutBinding.otpProgressBar.setVisibility(View.VISIBLE);
                }else {
                    otpLayoutBinding.signUpOtpInputText.setError("Enter Valid OTP");
                }
            }
        });

        //Resend otp on click listener
        otpLayoutBinding.resendOtpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resend<=2) {
                    otpLayoutBinding.resendOtpTv.setEnabled(false);
                    getOtp(authKey, mobile, email);
                    resend++;
                }else {
                    Toast.makeText(getContext(),"You've exceed the limit of Resend OTP",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Close Image Button click listener
        otpLayoutBinding.otpLayoutCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //Get Otp
    private void getOtp(String auth, String mobile, String emailId) {

        signUpViewModel.getOtp(auth, mobile, emailId).observe(this, new Observer<Otp>() {
            @Override
            public void onChanged(Otp otp) {

                mOtp = otp.getOtp();

                otpLayoutBinding.otpLayoutGroup.setVisibility(View.VISIBLE);
                otpLayoutBinding.otpProgressBar.setVisibility(View.GONE);
                new CountDownTimer(60000,1000){

                    @Override
                    public void onTick(long millisUntilFinished) {

                        otpLayoutBinding.resendOtpTv.setText("Resend OTP in " +  millisUntilFinished/1000);
                    }

                    @Override
                    public void onFinish() {

                        otpLayoutBinding.resendOtpTv.setText("Resend OTP");
                        otpLayoutBinding.resendOtpTv.setEnabled(true);
                    }
                }.start();
            }
        });
    }

    //Sign up user
    private void signUpUser(String shop,String name, String uEmail, String uMobile,String password){

        signUpViewModel.signUpUser(shop,name,uEmail,uMobile,password).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                Log.e(TAG,"" + s);
                if (s.equals("Success")){
                    logInUser(uMobile,password);
                }else {
                    dismiss();
                    Toast.makeText(getContext(),"Error " + s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Log In User
    private void logInUser(String id, String password) {

        Log.e(TAG,"Loggin In");
        apiService.savePost(id, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        Log.e(TAG,"Log in subscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(Post post) {

                        otpLayoutBinding.otpProgressBar.setVisibility(View.INVISIBLE);
                        otpLayoutBinding.otpLayoutGroup.setVisibility(View.VISIBLE);
                        Log.e(TAG, "Log in user on next: " + post.getLogin_id());

                        String session_id = post.getSession_id();
                        Log.e(TAG, "Session Id is " + session_id);

                        if (post.getMessage().equals(getResources().getString(R.string.retailer_access))) {

                            //Save User Id and password in Shared Preference
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.SAVE_ID_PASS, Context.MODE_PRIVATE).edit();
                            editor.putString(Constants.USER_ID, id);
                            editor.putString(Constants.PASSWORD, password);
                            editor.apply();

                            //Send User to HomeActivity
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.putExtra(Constants.SESSION_ID, session_id);
                            intent.putExtra(Constants.USER_ID, id);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Error: " + post.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        dismiss();
                        Log.e(TAG, "Error Login user");
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                        Log.e(TAG,"Log in request complete");
                    }
                });
    }
}

