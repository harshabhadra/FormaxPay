package com.rechargeweb.rechargeweb.SignUpLogIn;


import android.app.AlertDialog;
import android.content.Context;
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
import com.rechargeweb.rechargeweb.ForgetPassword;
import com.rechargeweb.rechargeweb.Keys;
import com.rechargeweb.rechargeweb.Model.Password;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.ForgetPassowordViewModel;
import com.rechargeweb.rechargeweb.databinding.LayoutCustomDialogBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPassSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {


    private static final String TAG = ForgetPassSheetFragment.class.getSimpleName();
    private String mobileNumber;
    private String authKey;
    private String session_id;
    private String otp, currentPassword, newPassword;
    private ForgetPassowordViewModel forgetPassowordViewModel;
    private LayoutCustomDialogBinding customDialogBinding;
    private AlertDialog loadingDialog;
    int resend = 0;

    public ForgetPassSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initializing DataBinding
       customDialogBinding = DataBindingUtil.inflate(inflater, R.layout.layout_custom_dialog, container, false);

        //Initializing ViewModel class
        forgetPassowordViewModel = ViewModelProviders.of(this).get(ForgetPassowordViewModel.class);

        //Initializing Auth key
        authKey = new Keys().apiKey();

        //Add Text Change Listener to enter mobileNumber edit text
        customDialogBinding.forgetPassEnterUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()==10) {
                    customDialogBinding.fpSendOtpButton.setEnabled(true);
                } else {
                    customDialogBinding.fpSendOtpButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Adding Text Watcher to otp edit text
        customDialogBinding.forgetPassEnterOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 6) {
                    customDialogBinding.forgetPassSubmitOtpButton.setEnabled(true);
                } else {
                    customDialogBinding.forgetPassSubmitOtpButton.setEnabled(false);
                }
            }
        });

        //Adding Text Change Listener to new Password text input
        customDialogBinding.fpNewPassTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                customDialogBinding.fpNewPassTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                customDialogBinding.fpNewPassTextInputLayout.setErrorEnabled(true);
            }
        });

        //Add Text Watcher to confirm password text input
        customDialogBinding.fpConfirmPassTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                customDialogBinding.fpConfirmPassTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

                customDialogBinding.fpConfirmPassTextInputLayout.setErrorEnabled(true);
            }
        });

        //Add On Click Listener to send otp button
        customDialogBinding.fpSendOtpButton.setOnClickListener(this);
        customDialogBinding.fpCloseIv.setOnClickListener(this);
        customDialogBinding.fpResendOtp.setOnClickListener(this);
        customDialogBinding.forgetPassSubmitOtpButton.setOnClickListener(this);
        customDialogBinding.fpPassWordSubmitButton.setOnClickListener(this);
        return customDialogBinding.getRoot();
    }

    @Override
    public void onClick(View v) {

        if (v.equals(customDialogBinding.fpSendOtpButton)){

            mobileNumber = customDialogBinding.forgetPassEnterUserId.getText().toString().trim();
            customDialogBinding.fpMobileNumberLayout.setVisibility(View.INVISIBLE);
            customDialogBinding.alertLoading.setVisibility(View.VISIBLE);
            forgetPassword(authKey,mobileNumber);
        }else if (v.equals(customDialogBinding.fpCloseIv)){
            dismiss();
        }else if (v.equals(customDialogBinding.fpResendOtp)){
            resend++;
            if (resend<=2) {
                forgetPassword(authKey, mobileNumber);
                customDialogBinding.fpResendOtp.setEnabled(false);
            }else {
                customDialogBinding.fpResendOtp.setText("You've exceed the limit to get OTP");
            }
        }else if (v.equals(customDialogBinding.forgetPassSubmitOtpButton)){

            String userOtp = customDialogBinding.forgetPassEnterOtp.getText().toString().trim();
            if (userOtp.equals(otp)){
                customDialogBinding.fpOtpLayout.setVisibility(View.GONE);
                customDialogBinding.fpPasswordGroup.setVisibility(View.VISIBLE);
            }
        }else if (v.equals(customDialogBinding.fpPassWordSubmitButton)){
            newPassword = customDialogBinding.fpNewPassTextInput.getText().toString().trim();
            String confirmPassword = customDialogBinding.fpConfirmPassTextInput.getText().toString().trim();

            if (newPassword == null){
                customDialogBinding.fpNewPassTextInputLayout.setError("Enter New Password");
            }else if (confirmPassword.isEmpty()){
                customDialogBinding.fpConfirmPassTextInputLayout.setError("Confirm Your Password");
            }else if (!newPassword.equals(confirmPassword)){
                customDialogBinding.fpConfirmPassTextInputLayout.setError("Password Mismatch");
            }else {
                loadingDialog = createLoadingDialog(getContext());
                setNewPassword(session_id,authKey,currentPassword,newPassword);
            }
        }
    }

    //Method to get otp after forget password
    private void forgetPassword(String authKey, String mobile) {

        forgetPassowordViewModel.getOtpForgetPassword(authKey, mobile).observe(this, new Observer<ForgetPassword>() {
            @Override
            public void onChanged(ForgetPassword forgetPassword) {

                if (forgetPassword != null) {
                    if (forgetPassword.getMessage().equals("Success")) {
                        Log.e(TAG, "Forget Password is not null");
                        customDialogBinding.alertLoading.setVisibility(View.INVISIBLE);
                        otp = forgetPassword.getOtp();
                        currentPassword = forgetPassword.getPassword();
                        session_id = forgetPassword.getSessionId();
                        customDialogBinding.fpMobileNumberLayout.setVisibility(View.GONE);
                        customDialogBinding.fpOtpLayout.setVisibility(View.VISIBLE);
                        setUpResendOtoTextView();
                    } else {
                        Log.e(TAG, "Forget Password message: " + forgetPassword.getMessage());
                        Toast.makeText(getContext(), forgetPassword.getMessage(), Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            }
        });
    }

    private void setUpResendOtoTextView(){
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                customDialogBinding.fpResendOtp.setText("Wait for 1 min");
            }

            @Override
            public void onFinish() {

                customDialogBinding.fpResendOtp.setText("Resend OTP");
                customDialogBinding.fpResendOtp.setEnabled(true);
            }
        }.start();
    }

    //Chnange Password
    private void setNewPassword(String session_id, String auth, String cPass, String nPass){

        ApiService apiService = ApiUtills.getApiService();
        apiService.changePassword(session_id,auth,cPass,nPass,nPass).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Password>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Password password) {

                        loadingDialog.dismiss();
                        Log.e(TAG,"Password Change : " + password.getMessage());
                        Toast.makeText(getContext(),"You've Successfully Changed Your Password",Toast.LENGTH_LONG).show();
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        loadingDialog.dismiss();
                        Log.e(TAG,"Error changing password: " + e.getMessage());
                        Toast.makeText(getContext(),"Erro Changing Passowrd, " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                    @Override
                    public void onComplete() {

                        loadingDialog.dismiss();
                    }
                });
    }

    //Create Loading Dialog
    private AlertDialog createLoadingDialog(Context context){
        View view = getLayoutInflater().inflate(R.layout.loading_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(view);
        return builder.create();
    }
}
