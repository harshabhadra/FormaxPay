package com.rechargeweb.rechargeweb.SignUpLogIn;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.atom.mpsdklibrary.PayActivity;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.BottomSheetFrag.OtpFragment;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.SignUpViewModel;
import com.rechargeweb.rechargeweb.databinding.FragmentSignUpBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment{

    private static final String TAG = SignUpFragment.class.getSimpleName();

    private String shopName, userName, mobileNumber, userEmail;

    private String uPassword, uConfirmPassword;

    private AlertDialog loadingDialog;
    private SignUpViewModel signUpViewModel;
    private String mOtp;
    private String authKey;
    private FragmentSignUpBinding signUpBinding;
    private ApiService apiService;
    private boolean endProcess;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initializing DataBinding
        signUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        View view = signUpBinding.getRoot();

        //Initializing ApiService
        apiService = ApiUtills.getApiService();

        //Initializing Auth key
        authKey = getResources().getString(R.string.auth_key);


        endProcess = false;

        //Initializing ViewModel
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        //Adding TextChangeListener to ShopNameLayout
        signUpBinding.signUpShopNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                signUpBinding.signUpShopNameLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

                signUpBinding.signUpShopNameLayout.setErrorEnabled(true);
            }
        });

        //Adding TextChangeListener to UserFulNameLayout
        signUpBinding.signUpUserNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                signUpBinding.signUpUserNameLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

                signUpBinding.signUpUserNameLayout.setErrorEnabled(true);
            }
        });

        //Adding TextChangeListener to PhoneNumberLayout
        signUpBinding.signUpMobileInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                signUpBinding.signUpMobileLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

                signUpBinding.signUpMobileLayout.setErrorEnabled(true);
            }
        });

        //Adding TextChangeListener to Email Layout
        signUpBinding.signUpUserEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                signUpBinding.signUpUserEmailLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

                signUpBinding.signUpUserEmailLayout.setErrorEnabled(true);
            }
        });

        //Adding TextChangeListener to PasswordTextInputLayout
        signUpBinding.signUpPasswordInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                signUpBinding.signUpPasswordLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

                signUpBinding.signUpPasswordLayout.setErrorEnabled(true);
            }
        });

        //Adding TextChangeListener to ConfirmPasswordLayout
        signUpBinding.signUpConfirmPasswordInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                signUpBinding.signUpConfirmPasswordLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

                signUpBinding.signUpConfirmPasswordLayout.setErrorEnabled(true);
            }
        });

        //Set onClickListener to Next button
        signUpBinding.signUpNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null){
                    inputMethodManager.hideSoftInputFromWindow(signUpBinding.signUpNextButton.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                //Getting required values
                shopName = signUpBinding.signUpShopNameInput.getText().toString();
                userName = signUpBinding.signUpUserNameInput.getText().toString();
                mobileNumber = signUpBinding.signUpMobileInput.getText().toString();
                userEmail = signUpBinding.signUpUserEmailInput.getText().toString();

                if (shopName.isEmpty()) {
                    signUpBinding.signUpShopNameLayout.setError("Enter Shop Name");
                } else if (userName.isEmpty()) {
                    signUpBinding.signUpUserNameLayout.setError("Enter Your Name");
                } else if (mobileNumber.isEmpty() || !(mobileCheck(mobileNumber))) {
                    signUpBinding.signUpMobileLayout.setError("Enter Valid Mobile Number");
                } else if (userEmail.isEmpty() || !(isValidEmail(userEmail))) {
                    signUpBinding.signUpUserEmailLayout.setError("Enter Valid Email Id");
                } else {
                    signUpBinding.signUpShopNameInput.getText().clear();
                    signUpBinding.signUpUserNameInput.getText().clear();
                    signUpBinding.signUpMobileInput.getText().clear();
                    signUpBinding.signUpUserEmailInput.getText().clear();
                    signUpBinding.signUpGroupDetails.setVisibility(View.GONE);
                    signUpBinding.signUpGroupPassword.setVisibility(View.VISIBLE);
                }
            }
        });

        //Set onClickListener to sign-up button
        signUpBinding.createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null){
                    inputMethodManager.hideSoftInputFromWindow(signUpBinding.createAccountButton.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }

                //Getting password
                uPassword = signUpBinding.signUpPasswordInputText.getText().toString().trim();
                uConfirmPassword = signUpBinding.signUpConfirmPasswordInputText.getText().toString().trim();

                if (uPassword == null){
                    signUpBinding.signUpPasswordLayout.setError("Enter Password");
                }else if (uConfirmPassword.isEmpty()){
                    signUpBinding.signUpConfirmPasswordLayout.setError("Confirm Your Password");
                }else if (!uPassword.equals(uConfirmPassword)){
                    signUpBinding.signUpConfirmPasswordLayout.setError("Password Mismatch");
                    signUpBinding.signUpPasswordLayout.setError("Password Mismatch");
                }else {
                    signUpBinding.signUpPasswordInputText.getText().clear();
                    signUpBinding.signUpConfirmPasswordInputText.getText().clear();
                    signUpBinding.signUpGroupPassword.setVisibility(View.GONE);
                    signUpBinding.signUpGroupDetails.setVisibility(View.VISIBLE);
                    OtpFragment otpFragment = new OtpFragment(authKey,mobileNumber,userEmail,uPassword,shopName,userName);
                    otpFragment.setCancelable(false);
                    otpFragment.show(getFragmentManager().beginTransaction(),otpFragment.getTag());
                }
            }
        });

        return view;
    }

    //Check if the mobile no. is correct
    private boolean mobileCheck(String s) {
        Pattern pattern = Pattern.compile("(0/91)?[6-9][0-9]{9}");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    //Check if the Email is valid
    public boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
