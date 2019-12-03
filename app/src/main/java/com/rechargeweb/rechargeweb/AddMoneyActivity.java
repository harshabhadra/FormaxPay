package com.rechargeweb.rechargeweb;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.atom.mpsdklibrary.PayActivity;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.databinding.ActivityAddMoneyBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AddMoneyActivity extends AppCompatActivity {

    ActivityAddMoneyBinding activityAddMoneyBinding;
    private String amount;
    private String date;
    private Double payAmount;
    private String customerAcc;
    private String session_id;
    private String clientCode;
    private String txnId;
    private static final String TAG = AddMoneyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        //Getting intent
        session_id = getIntent().getStringExtra(Constants.SESSION_ID);
        clientCode = "00" + session_id;
        txnId = getTxnId();

        //Initializing DataBinding
        activityAddMoneyBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_money);

        //Getting date
        date = getCurrentTime();

        customerAcc = getCustomerAcc();
        Log.e(TAG,"Acc: " + customerAcc);

        //Add text watcher to amount text input layout
        activityAddMoneyBinding.addMoneyTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                activityAddMoneyBinding.textInputLayoutAddMoney.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

                activityAddMoneyBinding.textInputLayoutAddMoney.setErrorEnabled(true);
                if (!s.toString().isEmpty()) {
                    if (!isValidAmount(s.toString())) {
                        activityAddMoneyBinding.textInputLayoutAddMoney.setError("Enter amount between 50 to 15000");
                    } else {
                        activityAddMoneyBinding.addMoneyButton.setEnabled(true);
                    }
                }

            }
        });
        //Add on click listener to add money button
        activityAddMoneyBinding.addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = activityAddMoneyBinding.addMoneyTextInput.getText().toString().trim();
                if (!amount.isEmpty()) {
                    payAmount = Double.parseDouble(amount);
                    Log.e(TAG, "Amount: " + payAmount);
                    if (payAmount != null && date != null){
                        startPaymentGateway(date,payAmount,customerAcc, clientCode,txnId);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
        {
            System.out.println("---------INSIDE-------");
            if (data != null)
            {
                String message = data.getStringExtra("status");
                String[] resKey = data.getStringArrayExtra("responseKeyArray");
                String[] resValue = data.getStringArrayExtra("responseValueArray");
                if(resKey!=null &&resValue!=null)
                {
                    for(int i=0; i<resKey.length; i++)
                        Log.e(TAG,"  "+i+" resKey : "+resKey[i]+" resValue : "+resValue[i]);
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Log.e(TAG,"RECEIVED BACK--->" + message);
            }

        }
    }

    private void startPaymentGateway(String pDatem, Double pAmount, String cusAcc,String id,String tranId) {
        Intent newPayIntent = new Intent(this, PayActivity.class);
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
        startActivityForResult(newPayIntent, 1);
    }

    public String encodeBase64(String encode) {
        String decode = null;

        try {


            decode = Base64.encodeToString(encode.getBytes(), Base64.DEFAULT);
        } catch (Exception e) {
            System.out.println("Unable to decode : " + e);
        }
        return decode;
    }

    //Is valid amount
    private boolean isValidAmount(String amt) {
        int a = Integer.parseInt(amt);
        return a >= 50 && a <= 15000;
    }

    //Generate current date and time
    private String getCurrentTime() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(new Date());
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
}
