package com.rechargeweb.rechargeweb.SignUpLogIn;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.Group;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Activity.SignUpActivity;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Model.Post;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.Model.Otp;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.SignUpViewModel;
import com.rechargeweb.rechargeweb.databinding.FragmentSignUpBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

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
                    loadingDialog = createLoadingDialog(getContext());
                    getOtp(authKey, mobileNumber, userEmail);
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
                    loadingDialog = createLoadingDialog(getContext());
                    loadingDialog.show();
                    signUpUser(shopName,userName,userEmail,mobileNumber,uPassword);
                }
            }
        });

        return view;
    }

    //Get Otp
    private void getOtp(String auth, String mobile, String emailId) {
        loadingDialog.show();
        signUpViewModel.getOtp(auth, mobile, emailId).observe(SignUpFragment.this, new Observer<Otp>() {
            @Override
            public void onChanged(Otp otp) {
                loadingDialog.dismiss();
                if (otp != null) {
                    mOtp = otp.getOtp();
                    if (!mOtp.isEmpty()) {
                        createOtpVerifyDialog(getContext(), mOtp);
                    } else {
                        Intent intent = new Intent(getActivity(),SignUpActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(intent);
                        getActivity().finish();
                        Toast.makeText(getContext(),"" + otp.getMessage(),Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e(TAG, "OTP is null");
                }
            }
        });
    }

    //Create otp verification dialog
    private void createOtpVerifyDialog(Context context, String op) {
        View layout = getLayoutInflater().inflate(R.layout.enter_otp_layout, null);
        TextInputLayout otpLayout = layout.findViewById(R.id.sign_up_otp_layout);
        TextInputEditText otpEditText = layout.findViewById(R.id.sign_up_otp_input_text);
        Button verifyButton = layout.findViewById(R.id.otp_verify_button);
        ImageView closeImage = layout.findViewById(R.id.otp_layout_close_iv);
        TextView resendTextVeiw = layout.findViewById(R.id.resend_otp_tv);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        //Make resend otp visible after 60 seconds
        new CountDownTimer(30000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                resendTextVeiw.setText("Resend OTP in " + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                resendTextVeiw.setText("Resend OTP");
                resendTextVeiw.setEnabled(true);
            }
        }.start();

        //Adding TextWatcher to OTP layout
        otpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                otpLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                otpLayout.setErrorEnabled(true);
            }
        });

        //Adding onClickListener to verifyOtpButton
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null){
                    inputMethodManager.hideSoftInputFromWindow(verifyButton.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }

                String otp = otpEditText.getText().toString().trim();
                if (!otp.isEmpty() && otp.equals(op)) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Your Mobile Number is Verified", Toast.LENGTH_SHORT).show();
                    signUpBinding.signUpGroupDetails.setVisibility(View.GONE);
                    signUpBinding.signUpGroupPassword.setVisibility(View.VISIBLE);
                } else {
                    otpLayout.setError("Enter 6 digit OTP");
                }
            }
        });

        resendTextVeiw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getOtp(authKey,mobileNumber,userEmail);
            }
        });

        //Close the layout
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!endProcess){
                    Toast.makeText(getContext(),"Press Again to End Sign Up Process",Toast.LENGTH_LONG).show();
                    endProcess = true;
                }else {
                    Intent intent = new Intent(getActivity(),SignUpActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }

    //Sign up user
    private void signUpUser(String shop,String name, String uEmail, String uMobile,String password){

        signUpViewModel.signUpUser(shop,name,uEmail,uMobile,password).observe(SignUpFragment.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                Log.e(TAG,"" + s);
                if (s.equals("Success")){
                    logInUser(uMobile,password);
                }else {
                    loadingDialog.dismiss();
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

                        loadingDialog.dismiss();
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

                        Log.e(TAG, "Error Login user");
                        loadingDialog.dismiss();
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                        Log.e(TAG,"Log in request complete");
                    }
                });
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

    //Create Loading Dialog
    private AlertDialog createLoadingDialog(Context context) {

        View view = getLayoutInflater().inflate(R.layout.loading_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(view);
        return builder.create();
    }
}
