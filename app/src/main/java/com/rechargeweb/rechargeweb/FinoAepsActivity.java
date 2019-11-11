package com.rechargeweb.rechargeweb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.easypay.epmoney.epmoneylib.baseframework.model.PaisaNikalConfiguration;
import com.easypay.epmoney.epmoneylib.baseframework.model.PaisaNikalRequest;
import com.easypay.epmoney.epmoneylib.response_model.PaisaNikalTransactionResponse;
import com.easypay.epmoney.epmoneylib.ui.activity.IntermidiateActivity;
import com.easypay.epmoney.epmoneylib.utils.PaisaNikalConfig;
import com.rechargeweb.rechargeweb.databinding.ActivityFinoAepsBinding;

import org.apache.commons.codec.DecoderException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FinoAepsActivity extends AppCompatActivity {

    private static final String TAG = FinoAepsActivity.class.getSimpleName();
    private final static String CONFIG_ENVIRONMENT = PaisaNikalConfig.Config.ENVIRONMENT_PRODUCTION;
    private String CONFIG_AGENT_ID_CODE = "FORMAX01";//RS00789
    private String CONFIG_AGENT_NMAE = "Agent name"; //Agent name
    private PaisaNikalConfiguration config = null;
    private PaisaNikalRequest apiRequest = null;

    private static final Integer CODE_AEPS_TRANSACTION = 1010;
    private static final Integer CODE_MICRO_TRANSACTION = 1011;

    String mobileNumber;
    String amount;
    private boolean isBalanceCheck;

    ActivityFinoAepsBinding finoAepsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fino_aeps);

        config = new PaisaNikalConfiguration();
        config.setAgentId(CONFIG_AGENT_ID_CODE);
        config.setAgentName(CONFIG_AGENT_NMAE);
        config.setEnvironment(CONFIG_ENVIRONMENT);

        //Initializing dataBinding
        finoAepsBinding = DataBindingUtil.setContentView(this,R.layout.activity_fino_aeps);

        //balance inquiry
        finoAepsBinding.balanceInquiryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isBalanceCheck = true;
                finoAepsBinding.cashWithdrawalLayout.setVisibility(View.GONE);
                finoAepsBinding.balanceInquiryLayout.setVisibility(View.GONE);
                finoAepsBinding.finoMobileNumberLayout.setVisibility(View.VISIBLE);
                finoAepsBinding.finoSubmitButton.setVisibility(View.VISIBLE);
            }
        });

        //Cash withdrawal
        finoAepsBinding.cashWithdrawalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isBalanceCheck = false;
                finoAepsBinding.cashWithdrawalLayout.setVisibility(View.GONE);
                finoAepsBinding.balanceInquiryLayout.setVisibility(View.GONE);
                finoAepsBinding.finoMobileNumberLayout.setVisibility(View.VISIBLE);
                finoAepsBinding.finoAmountLayout.setVisibility(View.VISIBLE);
                finoAepsBinding.finoSubmitButton.setVisibility(View.VISIBLE);
            }
        });

        //on Submit button click
        finoAepsBinding.finoSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isBalanceCheck) {
                    mobileNumber = finoAepsBinding.finoMobileNumberTextInput.getText().toString().trim();
                    amount = finoAepsBinding.finoAmountTextInput.getText().toString().trim();

                    if (mobileNumber.isEmpty()) {

                        finoAepsBinding.finoMobileNumberLayout.setError("Enter Mobile Number");
                    }else if (amount.isEmpty()){
                        finoAepsBinding.finoAmountLayout.setError("Enter Amount");
                    }else {

                        cashWithDrawal(mobileNumber, amount);
                    }
                }else {
                    mobileNumber = finoAepsBinding.finoMobileNumberTextInput.getText().toString().trim();

                    if (mobileNumber.isEmpty()){
                        finoAepsBinding.finoMobileNumberLayout.setError("Enter Mobile Number");
                    }else {
                        checkBalance(mobileNumber);
                    }
                }
            }
        });
    }

    //Fino Aeps balance
    private void checkBalance(String mobileNumber){
        apiRequest = new PaisaNikalRequest();
        apiRequest.setAmount("50000"); //Transaction Amount
        try {
            apiRequest.setCheckSum(Checksum.getCheckSum(CONFIG_AGENT_ID_CODE));
        } catch (DecoderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder  sb = new StringBuilder();
        sb.append(PaisaNikalConfig.ApiTransactionId.YBL_AEPS__BALANCE_INQUIRY);
        sb.append(System.currentTimeMillis());

        apiRequest.setMobileNumber(mobileNumber);
        apiRequest.setOrderId(sb.toString());
        apiRequest.setTimeStemp(Checksum.currentTime.toString());
        apiRequest.setToken(Checksum.randomNumber);
        apiRequest.setServiceCode(String.valueOf(PaisaNikalConfig.ApiServices.YBL_AEPS__BALANCE_INQUIRY));

        Intent intent = new Intent(this, IntermidiateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_CONFIG, config);
        bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_REQUEST, apiRequest);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_MICRO_TRANSACTION);
    }

    //Fino AEPS withdrawal
    private void cashWithDrawal(String mobileNumber, String amount){
        //Aeps withdrawal cash request
        apiRequest = new PaisaNikalRequest();
        apiRequest.setAmount(amount); //Transaction Amount
        try {
            apiRequest.setCheckSum(Checksum.getCheckSum(CONFIG_AGENT_ID_CODE));
        } catch (DecoderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder  sb = new StringBuilder();
        sb.append(PaisaNikalConfig.ApiTransactionId.YBL_AEPS__CASH_WITHDRAW);
        sb.append(System.currentTimeMillis());

        apiRequest.setMobileNumber(mobileNumber);
        apiRequest.setOrderId(sb.toString());
        apiRequest.setTimeStemp(Checksum.currentTime.toString());
        apiRequest.setToken(Checksum.randomNumber);
        apiRequest.setServiceCode(String.valueOf(PaisaNikalConfig.ApiServices.YBL_AEPS__CASH_WITHDRAW));

        Intent intent = new Intent(this, IntermidiateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_CONFIG, config);
        bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_REQUEST, apiRequest);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_MICRO_TRANSACTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_MICRO_TRANSACTION && resultCode == Activity.RESULT_OK){
            if (data!= null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    PaisaNikalTransactionResponse apiResponse= bundle.getParcelable(PaisaNikalConfig.UI.FINO_TRANSACTION_RESPONSE);
                    Log.e(TAG, "Response: "+apiResponse);
                    if (apiResponse!= null && apiResponse.getRESPCODE() == 200){
                        //Success handler
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                    }else{
                        //Fail handler
                        Toast.makeText(this, "Error: " + apiResponse.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }else if (requestCode == CODE_MICRO_TRANSACTION && resultCode == Activity.RESULT_CANCELED){
            //handler for user canceled
            Toast.makeText(this, "Request has been canceled by user", Toast.LENGTH_SHORT).show();
        }
    }
}
