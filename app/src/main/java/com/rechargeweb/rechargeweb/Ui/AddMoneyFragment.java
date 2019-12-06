package com.rechargeweb.rechargeweb.Ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atom.mpsdklibrary.PayActivity;
import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.AddMoneyTermsAdapters;
import com.rechargeweb.rechargeweb.Constant.DummyData;
import com.rechargeweb.rechargeweb.Model.Items;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.databinding.FragmentAddMoneyBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMoneyFragment extends Fragment{

    private String amount;
    private String date;
    private Double payAmount;
    private String customerAcc;
    private String session_id;
    private String clientCode;
    private String txnId;
    private static final String TAG = AddMoneyFragment.class.getSimpleName();
    private FragmentAddMoneyBinding activityAddMoneyBinding;
    private static final int RC_PAYMENT_GATEWAY = 1;
    private AddMoneyTermsAdapters addMoneyTermsAdapters;

    public AddMoneyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activityAddMoneyBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_money,container,false);
        View view = activityAddMoneyBinding.getRoot();

        DummyData dummyData = new DummyData();

        //Setting up terms recyclerView
        activityAddMoneyBinding.addMoneyTermsRecycler.setHasFixedSize(true);
        activityAddMoneyBinding.addMoneyTermsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        addMoneyTermsAdapters = new AddMoneyTermsAdapters(getContext(),dummyData.getAddMoneyTerms());
        activityAddMoneyBinding.addMoneyTermsRecycler.setAdapter(addMoneyTermsAdapters);

        //Get Activity
        HomeActivity activity = (HomeActivity) getActivity();
        if (activity != null) {
            session_id = activity.getSession_id();
            clientCode = "00" + session_id;
            txnId = getTxnId();
        }

        //Getting date
        date = getCurrentTime();

        customerAcc = getCustomerAcc();
        Log.e(TAG,"Acc: " + customerAcc);

        //Adding TextWatcher to AmountTextInputLayout
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

        //Set OnClick Listener to the AddMoney Button
        activityAddMoneyBinding.addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = activityAddMoneyBinding.addMoneyTextInput.getText().toString().trim();
                if (!amount.isEmpty() && isValidAmount(amount)) {
                    payAmount = Double.parseDouble(amount);
                    Log.e(TAG, "Amount: " + payAmount);
                    if (payAmount != null && date != null){
                        startPaymentGateway(date,payAmount,customerAcc, clientCode,txnId);
                    }
                }else {
                    activityAddMoneyBinding.textInputLayoutAddMoney.setError("Enter Amount between 50 to 15000");
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PAYMENT_GATEWAY)
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
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                Log.e(TAG,"RECEIVED BACK--->" + message);
                showCallbackMessage(message);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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

    //Create Alert Dialog to show callback message
    private void showCallbackMessage(String message){

        View layout = getLayoutInflater().inflate(R.layout.add_money_dialog_layout,null);
        TextView messageTv = layout.findViewById(R.id.add_money_message_tv);
        TextView amountTv = layout.findViewById(R.id.add_money_amount_tv);
        Button button = layout.findViewById(R.id.add_money_dialog_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);
        builder.setView(layout);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        messageTv.setText(message);
        amountTv.setText("Amount: " + activityAddMoneyBinding.addMoneyTextInput.getText().toString().trim());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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

    //Is valid amount
    private boolean isValidAmount(String amt) {
        int a = Integer.parseInt(amt);
        return a >= 50 && a <= 15000;
    }
}
