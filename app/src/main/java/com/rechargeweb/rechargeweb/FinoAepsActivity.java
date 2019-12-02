package com.rechargeweb.rechargeweb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.easypay.epmoney.epmoneyaeps.baseframework.model.PaisaNikalConfiguration;
import com.easypay.epmoney.epmoneyaeps.baseframework.model.PaisaNikalRequest;
import com.easypay.epmoney.epmoneyaeps.response_model.PaisaNikalTransactionResponse;
import com.easypay.epmoney.epmoneyaeps.ui.activity.IntermidiateActivity;
import com.easypay.epmoney.epmoneyaeps.utils.PaisaNikalConfig;
import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.ViewModels.AllReportViewModel;
import com.rechargeweb.rechargeweb.databinding.ActivityFinoAepsBinding;

import org.apache.commons.codec.DecoderException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FinoAepsActivity extends AppCompatActivity {

    private static final String TAG = FinoAepsActivity.class.getSimpleName();
    private final static String CONFIG_ENVIRONMENT = PaisaNikalConfig.Config.ENVIRONMENT_PRODUCTION;
    private String CONFIG_AGENT_ID_CODE = "FORMAX01";//RS00789
    private String CONFIG_AGENT_NMAE = "Ezazul Haque"; //Agent name
    private PaisaNikalConfiguration config = null;
    private PaisaNikalRequest apiRequest = null;

    private static final Integer CODE_AEPS_TRANSACTION = 1010;
    private static final Integer CODE_MICRO_TRANSACTION = 1011;

    String mobileNumber;
    String amount;
    private boolean isBalanceCheck;
    int position;

    String session_id, user_id;
    String auth;
    String orderId;

    ActivityFinoAepsBinding finoAepsBinding;
    StringBuilder balanceBuilder, withdrawalBuilder;

    AllReportViewModel allReportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fino_aeps);


        auth = getResources().getString(R.string.auth_key);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.SESSION_ID)){
            session_id = intent.getStringExtra(Constants.SESSION_ID);
            user_id = intent.getStringExtra(Constants.USER_ID);
        }
        config = new PaisaNikalConfiguration();
        config.setAgentId(CONFIG_AGENT_ID_CODE);
        config.setAgentName(CONFIG_AGENT_NMAE);
        config.setEnvironment(CONFIG_ENVIRONMENT);


        allReportViewModel = ViewModelProviders.of(this).get(AllReportViewModel.class);

        isBalanceCheck = true;
        //Initializing dataBinding
        finoAepsBinding = DataBindingUtil.setContentView(this, R.layout.activity_fino_aeps);

        //Choose what user want to do
        finoAepsBinding.balanceInfoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBalanceCheck = true;
                finoAepsBinding.balanceInfoTv.setText(getResources().getString(R.string.balance_enquiry));

                if (finoAepsBinding.finoAmountTextInput.getText().length() > 0) {
                    finoAepsBinding.finoAmountTextInput.getText().clear();
                    finoAepsBinding.finoAmountTextInput.setEnabled(false);
                }

                String[] typeList = {getResources().getString(R.string.balance_enquiry),"Cash Withdrawal"};
                position = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(FinoAepsActivity.this);
                builder.setTitle("What you'd like to do?");
                builder.setSingleChoiceItems(typeList, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == position) {
                            isBalanceCheck = true;
                            finoAepsBinding.balanceInfoTv.setText(typeList[0]);
                            finoAepsBinding.finoAmountTextInput.setEnabled(false);
                            dialog.dismiss();
                        } else {
                            isBalanceCheck = false;
                            finoAepsBinding.balanceInfoTv.setText(typeList[1]);
                            finoAepsBinding.finoAmountTextInput.setEnabled(true);
                            dialog.dismiss();
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Add text change listener to mobile number
        finoAepsBinding.finoMobileNumberTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                finoAepsBinding.finoMobileNumberLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Add text change listener to amount
        finoAepsBinding.finoAmountTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                finoAepsBinding.finoAmountLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //on Submit button click
        finoAepsBinding.finoSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finoAepsBinding.finoAmountLayout.setErrorEnabled(true);
                finoAepsBinding.finoMobileNumberLayout.setErrorEnabled(true);

                if (!isBalanceCheck) {
                    mobileNumber = finoAepsBinding.finoMobileNumberTextInput.getText().toString().trim();
                    amount = finoAepsBinding.finoAmountTextInput.getText().toString().trim();

                    int value = Integer.parseInt(amount);
                    if (mobileNumber.isEmpty() || mobileNumber.length() < 10) {

                        finoAepsBinding.finoMobileNumberLayout.setError("Enter Valid Mobile Number");
                    } else if (value < 100 || value > 10000) {
                        finoAepsBinding.finoAmountLayout.setError("Enter Amount between 100 to 10000");
                    } else {
                        finoAepsBinding.finoAmountTextInput.getText().clear();
                        finoAepsBinding.finoMobileNumberTextInput.getText().clear();
                        withdrawalBuilder = new StringBuilder();
                        withdrawalBuilder.append(PaisaNikalConfig.ApiTransactionId.AEPS_CASH_WITHDRAW);
                        withdrawalBuilder.append(System.currentTimeMillis());
                        orderId = withdrawalBuilder.toString();

                        View view = getLayoutInflater().inflate(R.layout.loading_dialog, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(FinoAepsActivity.this);
                        builder.setCancelable(false);
                        builder.setView(view);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        allReportViewModel.sendAepsDetails(session_id,auth,"AW",amount,orderId,mobileNumber).observe(FinoAepsActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                dialog.dismiss();
                                if (s.equals("Success")){
                                    cashWithDrawal(mobileNumber, amount);
                                }else {
                                    Intent intent1 = new Intent(FinoAepsActivity.this, FinoAepsActivity.class);
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                    intent.putExtra(Constants.SESSION_ID, session_id);
                                    intent.putExtra(Constants.USER_ID, user_id);
                                    startActivity(intent1);
                                    finish();
                                }
                            }
                        });

                    }
                } else {
                    mobileNumber = finoAepsBinding.finoMobileNumberTextInput.getText().toString().trim();

                    if (mobileNumber.isEmpty() || mobileNumber.length() < 10) {
                        finoAepsBinding.finoMobileNumberLayout.setError("Enter Valid Mobile Number");
                    } else {
                        finoAepsBinding.finoMobileNumberTextInput.getText().clear();

                        balanceBuilder = new StringBuilder();
                        balanceBuilder.append(PaisaNikalConfig.ApiTransactionId.AEPS_BALANCE_INQUIRY);
                        balanceBuilder.append(System.currentTimeMillis());
                        orderId = balanceBuilder.toString();

                        Log.e(TAG,"Order Id: " + balanceBuilder.toString());

                        View view = getLayoutInflater().inflate(R.layout.loading_dialog, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(FinoAepsActivity.this);
                        builder.setCancelable(false);
                        builder.setView(view);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        allReportViewModel.sendAepsDetails(session_id,auth,"AB","0.00",orderId,mobileNumber).observe(FinoAepsActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                dialog.dismiss();
                                if (s.equals("Success")){
                                    checkBalance(mobileNumber);
                                }else {
                                    Intent intent1 = new Intent(FinoAepsActivity.this, FinoAepsActivity.class);
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                    intent.putExtra(Constants.SESSION_ID, session_id);
                                    intent.putExtra(Constants.USER_ID, user_id);
                                    startActivity(intent1);
                                    finish();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    //Fino Aeps balance
    private void checkBalance(String mobileNumber) {
        //Aeps balance check request
        apiRequest = new PaisaNikalRequest();
        //Transaction Amount, need to pass 0
        apiRequest.setAmount("0");
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

        //User entered mobile number
        //Mobile number should be start with >= 6 of first later
        apiRequest.setMobileNumber(mobileNumber);
        apiRequest.setOrderId(balanceBuilder.toString());
        apiRequest.setTimeStemp(Checksum.currentTime.toString());
        apiRequest.setToken(Checksum.randomNumber);
        apiRequest.setServiceCode(String.valueOf(PaisaNikalConfig.ApiServices.AEPS_BALANCE_INQUIRY));


        Intent intent = new Intent(this, IntermidiateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_CONFIG, config);
        bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_REQUEST, apiRequest);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_AEPS_TRANSACTION);
    }

    //Fino AEPS withdrawal
    private void cashWithDrawal(String mobileNumber, String amount) {
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

        apiRequest.setMobileNumber(mobileNumber);
        apiRequest.setOrderId(withdrawalBuilder.toString());
        apiRequest.setTimeStemp(Checksum.currentTime.toString());
        apiRequest.setToken(Checksum.randomNumber);
        apiRequest.setServiceCode(String.valueOf(PaisaNikalConfig.ApiServices.AEPS_CASH_WITHDRAW));


        Intent intent = new Intent(this, IntermidiateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_CONFIG, config);
        bundle.putParcelable(PaisaNikalConfig.Config.PAISA_NIKAL_REQUEST, apiRequest);
        intent.putExtras(bundle);
        startActivityForResult(intent, CODE_AEPS_TRANSACTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_MICRO_TRANSACTION && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    PaisaNikalTransactionResponse apiResponse = bundle.getParcelable(PaisaNikalConfig.UI.FINO_TRANSACTION_RESPONSE);
                    Log.e(TAG, "Response: " + apiResponse);
                    if (apiResponse != null && apiResponse.getRESPCODE() == 200) {
                        //Success handler
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        //Fail handler
                        Toast.makeText(this, "Error: " + apiResponse.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (requestCode == CODE_MICRO_TRANSACTION && resultCode == Activity.RESULT_CANCELED) {
            //handler for user canceled
            Toast.makeText(this, "Request has been canceled by user", Toast.LENGTH_SHORT).show();
        }else {
            Intent homeIntent = new Intent(FinoAepsActivity.this, HomeActivity.class);
            homeIntent.putExtra(Constants.SESSION_ID, session_id);
            homeIntent.putExtra(Constants.USER_ID, user_id);
            startActivity(homeIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(FinoAepsActivity.this, HomeActivity.class);
        homeIntent.putExtra(Constants.SESSION_ID, session_id);
        homeIntent.putExtra(Constants.USER_ID, user_id);
        startActivity(homeIntent);
        finish();
    }
}
