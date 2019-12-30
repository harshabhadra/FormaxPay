package com.rechargeweb.rechargeweb.BottomSheetFrag;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.atom.mpsdklibrary.PayActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Model.Otp;
import com.rechargeweb.rechargeweb.Model.Post;
import com.rechargeweb.rechargeweb.Network.ApiService;
import com.rechargeweb.rechargeweb.Network.ApiUtills;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.MainViewModel;
import com.rechargeweb.rechargeweb.ViewModels.SignUpViewModel;
import com.rechargeweb.rechargeweb.databinding.EnterOtpLayoutBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtpFragment extends BottomSheetDialogFragment {

    private static final String TAG = OtpFragment.class.getSimpleName();

    private static final int RC_PAYMENT_GATEWAY = 123;
    EnterOtpLayoutBinding otpLayoutBinding;
    private String authKey, mobile, email, password, shopName, userName;
    private SignUpViewModel signUpViewModel;

    private String mOtp;
    private String userOtp;
    private String currentDate, txnId, customerAcc,clientCode;
    private int resend = 0;
    private ApiService apiService;

    private MainViewModel mainViewModel;

    private String mmp_txn,mer_txn,resamount,prob,resdate,bank_txn,f_code,resclientCode,bank_name,
            authCode,ipg_txn_id,merchant_id,desc,discriminator,udf9,surcharge,cardNumber,udf1,udf2, udf3, udf4, udf5, signature;

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

        //Initializing MainViewModel
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //Initializing ApiServices
        apiService = ApiUtills.getApiService();

        currentDate = getCurrentTime();
        txnId = getTxnId();
        customerAcc = getCustomerAcc();
        clientCode = "001";

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
                    startPaymentGateway(currentDate,499.00,customerAcc,clientCode,txnId);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PAYMENT_GATEWAY) {
            System.out.println("---------INSIDE-------");
            if (data != null) {
                String message = data.getStringExtra("status");
                String[] resKey = data.getStringArrayExtra("responseKeyArray");
                String[] resValue = data.getStringArrayExtra("responseValueArray");
                if (resKey != null && resValue != null) {
                    resdate = resValue[0];
                    surcharge = resValue[1];
                    cardNumber = resValue[2];
                    prob = resValue[3];
                    resclientCode = resValue[4];
                    mmp_txn = resValue[5];
                    signature = resValue[6];
                    udf5 = resValue[7];
                    resamount = resValue[8];
                    udf9 = resValue[19];
                    udf3 = resValue[10];
                    merchant_id = resValue[11];
                    udf4 = resValue[12];
                    udf1 = resValue[13];
                    udf2 = resValue[14];
                    authCode = resValue[15];
                    discriminator = resValue[16];
                    mer_txn = resValue[17];
                    bank_txn = resValue[18];
                    ipg_txn_id = resValue[20];
                    bank_name = resValue[21];
                    desc = resValue[22];
                    f_code = resValue[23];

                    if (f_code.equals("success_00")){

                        f_code = "sign_up_success";
                    }else {
                        f_code = "sign_up_failed";
                    }
                    sendTransactionDetails(mobile, authKey,mmp_txn,mer_txn,resamount,prob,resdate,bank_txn,f_code,resclientCode
                            ,bank_name,authCode, ipg_txn_id, merchant_id,desc,discriminator,udf9,surcharge,cardNumber,udf1,udf2,udf3,udf4,udf5,signature);
                    for (int i = 0; i < resKey.length; i++)
                        Log.e(TAG, "  " + i + " resKey : " + resKey[i] + " resValue : " + resValue[i]);
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                Log.e(TAG, "RECEIVED BACK--->" + message);
                if (message.equals("Transaction Successful!")) {
                    signUpUser(shopName,userName,email,mobile,password);
                    otpLayoutBinding.otpLayoutGroup.setVisibility(View.INVISIBLE);
                    otpLayoutBinding.otpProgressBar.setVisibility(View.VISIBLE);
                }else {
                    dismiss();
                }
            }
        }
    }

    //Get Otp
    private void getOtp(String auth, String mobile, String emailId) {

        signUpViewModel.getOtp(auth, mobile, emailId).observe(this, new Observer<Otp>() {
            @Override
            public void onChanged(Otp otp) {

                mOtp = otp.getOtp();
                Log.e(TAG,"Otp is :" + mOtp);
                if (mOtp.isEmpty()){
                    dismiss();
                    Toast.makeText(getContext(),"Error: " + otp.getMessage(),Toast.LENGTH_LONG).show();
                }else {
                    otpLayoutBinding.otpLayoutGroup.setVisibility(View.VISIBLE);
                    otpLayoutBinding.otpProgressBar.setVisibility(View.GONE);
                    new CountDownTimer(60000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                            otpLayoutBinding.resendOtpTv.setText("Resend OTP in " + millisUntilFinished / 1000);
                        }

                        @Override
                        public void onFinish() {

                            otpLayoutBinding.resendOtpTv.setText("Resend OTP");
                            otpLayoutBinding.resendOtpTv.setEnabled(true);
                        }
                    }.start();
                }
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

    //Encode the clientCode
    public String encodeBase64(String encode) {
        String decode = null;

        try {
            decode = Base64.encodeToString(encode.getBytes(), Base64.DEFAULT);
        } catch (Exception e) {
            System.out.println("Unable to decode : " + e);
        }
        return decode;
    }
    //Method to start payment Gateway
    private void startPaymentGateway(String pDatem, Double pAmount, String cusAcc,String id,String tranId) {
        Intent newPayIntent = new Intent(getContext(), PayActivity.class);
        newPayIntent.putExtra("merchantId", "98617");
        //txnscamt Fixed. Must be 0
        newPayIntent.putExtra("txnscamt", "0");
        newPayIntent.putExtra("loginid", "98617");
        newPayIntent.putExtra("password", "074da5a7");
        newPayIntent.putExtra("prodid", "FORMAX");
        //txncurr Fixed. Must be �INR�
        newPayIntent.putExtra("txncurr", "INR");
        newPayIntent.putExtra("clientcode", encodeBase64(id));
        newPayIntent.putExtra("custacc", cusAcc);
        newPayIntent.putExtra("channelid", "INT");
        //amt  Should be 2 decimal number i.e 1.00
        newPayIntent.putExtra("amt", pAmount.toString());
        newPayIntent.putExtra("txnid", tranId);
        newPayIntent.putExtra("date", pDatem);
        newPayIntent.putExtra("signature_request", "910a5d112c4793154e");
        newPayIntent.putExtra("signature_response", "ba9dfcedd0c45dd02b");
        newPayIntent.putExtra("discriminator", "All");
        newPayIntent.putExtra("isLive", true);
        startActivityForResult(newPayIntent, RC_PAYMENT_GATEWAY);
    }

    //Generate customer account
    private String getCustomerAcc(){
        long timeS = System.currentTimeMillis()/1000;
        Random random = new Random();
        int r = random.nextInt(100);
        return  Long.toString(timeS) + String.valueOf(r);
    }

    //Generate txnId
    private String getTxnId(){
        long timeS = System.currentTimeMillis()/1000;
        Random random = new Random();
        int r = random.nextInt(100);
        return "FP" + timeS + r;
    }

    //Generate current date and time
    private String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    //Send transaction details
    private void sendTransactionDetails(String session_id, String authKey, String mmp_txn, String mer_txn, String amount, String prob, String date, String bank_txn,
                                        String f_code, String clientCode, String bank_name, String authCode, String ipg_txn_id, String merchant_id, String desc,
                                        String discriminator, String udf9, String surcharge, String cardNumber, String udf1, String udf2, String udf3, String udf4, String udf5,
                                        String signature){

        mainViewModel.sendTranReport(session_id, authKey,mmp_txn,mer_txn,resamount,prob,resdate,bank_txn,f_code,resclientCode
                ,bank_name,authCode, ipg_txn_id, merchant_id,desc,discriminator,udf9,surcharge,cardNumber,udf1,udf2,udf3,udf4,udf5,signature).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG,s);
            }
        });

    }
}

