package com.rechargeweb.rechargeweb.Ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rechargeweb.rechargeweb.Activity.HomeActivity;
import com.rechargeweb.rechargeweb.BottomSheetFrag.PaymentModeBottomSheet;
import com.rechargeweb.rechargeweb.Constant.DummyData;
import com.rechargeweb.rechargeweb.Keys;
import com.rechargeweb.rechargeweb.MoveToBankViewModel;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.Settlement;
import com.rechargeweb.rechargeweb.databinding.FragmentMoveMoneyToBankBinding;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoveMoneyToBankFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MoveMoneyToBankFragment.class.getSimpleName();
    private FragmentMoveMoneyToBankBinding fragmentMoveMoneyToBankBinding;
    private MoveToBankViewModel moveToBankViewModel;

    private String session_id, authKey, amount, paymentMode;
    private AlertDialog loadingDialgo;

    public MoveMoneyToBankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMoveMoneyToBankBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_move_money_to_bank,container,false);

        //Initialize ViewModel class
        moveToBankViewModel = ViewModelProviders.of(this).get(MoveToBankViewModel.class);

        //Initialize Auth key
        authKey = new Keys().apiKey();

        //Initializing Session_id
        HomeActivity homeActivity = (HomeActivity)getActivity();
        session_id = homeActivity.getSession_id();
        Log.e(TAG,"Session_id, " + session_id);

        //add Text Watcher To the amount text input layout
        fragmentMoveMoneyToBankBinding.moveToBankAmountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                fragmentMoveMoneyToBankBinding.moveToBankAmountTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

                fragmentMoveMoneyToBankBinding.moveToBankAmountTextInputLayout.setErrorEnabled(true);
                if (s.length()>0) {
                    if (!isValidAmount(s.toString())) {
                        fragmentMoveMoneyToBankBinding.moveToBankAmountTextInputLayout.setError("Enter Amount between 10000 to 10 Lacs");
                        fragmentMoveMoneyToBankBinding.moveToBankButton.setEnabled(false);
                    } else {
                        fragmentMoveMoneyToBankBinding.moveToBankButton.setEnabled(true);
                    }
                }
            }
        });

        //Set on Click listener to payment mode text view
        fragmentMoveMoneyToBankBinding.moveToBankPaymentModeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPaymentModeList();
            }
        });

        //Set on click listener to submit button
        fragmentMoveMoneyToBankBinding.moveToBankButton.setOnClickListener(this);

        return fragmentMoveMoneyToBankBinding.getRoot();
    }

    @Override
    public void onClick(View v) {

        if (v.equals(fragmentMoveMoneyToBankBinding.moveToBankButton)){

            amount = fragmentMoveMoneyToBankBinding.moveToBankAmountInput.getText().toString().trim();
            if (paymentMode != null){
                loadingDialgo = createLoadingDialog(getContext());
                loadingDialgo.show();
                sendMoneyToBank(session_id,authKey,paymentMode,amount);
            }else {
                fragmentMoveMoneyToBankBinding.moveToBankPaymentModeTv.setError("Select a Payment Mode");
            }

        }
    }

    //Method to send money to bank from Wallet Two
    private void sendMoneyToBank(String session_id,String authKey,String paymentMode, String amount){

        moveToBankViewModel.moveMoneyToBank(session_id,authKey,paymentMode,amount).observe(this, new Observer<Settlement>() {
            @Override
            public void onChanged(Settlement settlement) {

                loadingDialgo.dismiss();
                if (settlement != null){
                    Log.e(TAG,"Settlement response is full");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setTitle(settlement.getStatus());
                    builder.setMessage(settlement.getMessage());
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.main_container,new HomeFragment()).commit();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    //Method to show list of payment mode
    private void showPaymentModeList(){

        List<String>paymentModeList = new DummyData().getPaymentMode();
        String[]paymentModeArray = paymentModeList.toArray(new String[0]);
        Log.e(TAG,"Payment Mode array size: " + paymentModeArray.length);
        int position = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Payment Mode");
        builder.setSingleChoiceItems(paymentModeArray, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                paymentMode = paymentModeArray[which];
                fragmentMoveMoneyToBankBinding.moveToBankPaymentModeTv.setText(paymentModeArray[which]);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Check if the amount is valid
    private boolean isValidAmount(String amount){

        int amt = Integer.valueOf(amount);

        return amt >= 10000 && amt <= 1000000;
    }

    //Create Loading Dialog
    private AlertDialog createLoadingDialog(Context context){

        View layout = getLayoutInflater().inflate(R.layout.loading_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layout);
        builder.setCancelable(false);
        return builder.create();
    }
}
