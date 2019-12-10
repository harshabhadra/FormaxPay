package com.rechargeweb.rechargeweb.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Model.Register;
import com.rechargeweb.rechargeweb.Model.Validate;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.DmtViewModel;
import com.rechargeweb.rechargeweb.databinding.ActivityValidateBinding;

public class ValidateActivity extends AppCompatActivity {


    ActivityValidateBinding validateBinding;
    DmtViewModel dmtViewModel;

    String mobile;
    String message;
    String auth;
    String remitter_Id;
    private static final String TAG = ValidateActivity.class.getSimpleName();

    boolean isValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);

        validateBinding = DataBindingUtil.setContentView(this,R.layout.activity_validate);
        dmtViewModel = ViewModelProviders.of(this).get(DmtViewModel.class);

        auth = getResources().getString(R.string.auth_key);
        Intent intent = getIntent();

        if (intent.hasExtra(Constants.REMITTER_MOBILE)){

            message = intent.getStringExtra(Constants.MESSAGE);
            mobile = intent.getStringExtra(Constants.REMITTER_MOBILE);

            validateBinding.mobileNumberTextInput.setText(mobile);
            if (message.equals("OTP sent successfully")){

                validateBinding.button.setText("Validate");
                getRemmiterId(auth,mobile);
                isValidate = true;
            }else {
                validateBinding.button.setText("Register");
                validateBinding.group.setVisibility(View.VISIBLE);
                isValidate = false;
            }
        }

        validateBinding.changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent dmtIntent = new Intent(ValidateActivity.this,DmtActivity.class);
                startActivity(dmtIntent);
                finish();
            }
        });
        validateBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidate){

                    String otp = validateBinding.otpTextInput.getText().toString().trim();
                    if (otp.isEmpty()){
                        validateBinding.otpLayout.setError("Please Enter OTP");
                    }else {
                        validateBinding.validateLoading.setVisibility(View.VISIBLE);
                        validateBinding.button.setVisibility(View.GONE);
                        validateNumber(auth,mobile,remitter_Id,otp);
                    }
                }else {
                    String first = validateBinding.firstNameEditText.getText().toString().trim();
                    String last  = validateBinding.dmtLastNameEdit.getText().toString().trim();
                    String pin = validateBinding.pincodeTextInput.getText().toString().trim();

                    if (first.isEmpty()){
                        validateBinding.firstNameLayout.setError("Enter First Name");
                    }else if (last.isEmpty()){
                        validateBinding.lastNameLayout.setError("Enter Last Name");
                    }else if (pin.isEmpty()){
                        validateBinding.pincodeLayout.setError("Enter Pincode");
                    }else {
                        validateBinding.validateLoading.setVisibility(View.VISIBLE);
                        validateBinding.button.setVisibility(View.GONE);
                        registerUser(auth,mobile,first,last,pin);
                    }
                }

            }
        });
    }

    private void getRemmiterId(String auth,String mobile){

        dmtViewModel.getId(auth,mobile).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                remitter_Id = s;
                Log.e(TAG,"Remitter id is: "  + remitter_Id);
                validateBinding.otpLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void validateNumber(String auth,String mobile, String remitter_Id,String otp){

        dmtViewModel.validateUser(auth,mobile,remitter_Id,otp).observe(this, new Observer<Validate>() {
            @Override
            public void onChanged(Validate validate) {

                validateBinding.validateLoading.setVisibility(View.GONE);
                validateBinding.button.setVisibility(View.VISIBLE);
                if (validate != null){

                    String status = validate.getMessage();
                    if (status.equals("Transaction Successful")){
                        Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ValidateActivity.this, DmtActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        validateBinding.otpLayout.setError(status);
                        Intent intent = new Intent(ValidateActivity.this, ValidateActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        intent.putExtra(Constants.REMITTER_MOBILE,mobile);
                        intent.putExtra(Constants.MESSAGE,message);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private void registerUser(String auth, final String mobile, String first, String last, String pin){

        dmtViewModel.registerUser(auth,mobile,first,last,pin).observe(this, new Observer<Register>() {
            @Override
            public void onChanged(Register register) {
                validateBinding.validateLoading.setVisibility(View.GONE);
                validateBinding.button.setVisibility(View.VISIBLE);
                if (register != null){

                    if (register.getMessage().equals("OTP sent successfully")){
                        Intent intent = new Intent(ValidateActivity.this, ValidateActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        intent.putExtra(Constants.MESSAGE,register.getMessage());
                        intent.putExtra(Constants.REMITTER_MOBILE,mobile);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),register.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
