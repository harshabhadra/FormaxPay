package com.rechargeweb.rechargeweb.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rechargeweb.rechargeweb.Activity.RemitterActivity;
import com.rechargeweb.rechargeweb.Constant.Constants;
import com.rechargeweb.rechargeweb.Model.AddBeneficiary;
import com.rechargeweb.rechargeweb.Model.Beneficiary;
import com.rechargeweb.rechargeweb.R;
import com.rechargeweb.rechargeweb.ViewModels.BeneficiaryViewModel;

import java.util.ArrayList;
import java.util.List;

public class BeneficiaryAdapter extends RecyclerView.Adapter<BeneficiaryAdapter.BeneficiaryViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Beneficiary>beneficiaryList = new ArrayList<>();
    private BeneficiaryViewModel beneficiaryViewModel;
    String auth;
    String benId;
    String remId = "";

    View deleteBenLayout;
    View sendMoneyLayout;
    View delBenConfirmLayout;

    String session_id;
    String mobileNumebr;
    String name;
    String ifsc;
    String account;

    public BeneficiaryAdapter(Context context,String auth,String session_id,String mobileNumebr) {

        inflater = LayoutInflater.from(context);
        this.auth = auth;
        this.context = context;
        this.session_id = session_id;
        this.mobileNumebr = mobileNumebr;
        beneficiaryViewModel = ViewModelProviders.of((RemitterActivity)context).get(BeneficiaryViewModel.class);
    }

    @NonNull
    @Override
    public BeneficiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BeneficiaryViewHolder view =  new BeneficiaryViewHolder(inflater.inflate(R.layout.beneficiary_item,parent,false));

        deleteBenLayout = inflater.inflate(R.layout.delete_beneficiary_layout, null);
        sendMoneyLayout = inflater.inflate(R.layout.transfer_money,null);
        delBenConfirmLayout = inflater.inflate(R.layout.ben_delete_confirm_layout,null);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull BeneficiaryViewHolder holder, final int position) {

        if (beneficiaryList != null){
            name = beneficiaryList.get(position).getLastSuccessName();
            ifsc = beneficiaryList.get(position).getIfsc();
            account = beneficiaryList.get(position).getAccount();
            benId = beneficiaryList.get(position).getId();
            holder.sucessName.setText(name);
            holder.accountNum.setText("A/C: "+account);
            holder.ifscCode.setText("IFSC: " + ifsc);
            holder.successDate.setText("Last Success: " + beneficiaryList.get(position).getLast_success_date());
        }

        holder.transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextInputLayout textInputLayout = sendMoneyLayout.findViewById(R.id.textInputLayout_send_money);
                final TextInputEditText amontEd = sendMoneyLayout.findViewById(R.id.send_money_text_input);

                final Button sendButton = sendMoneyLayout.findViewById(R.id.send_money_button);
                final ProgressBar loading = sendMoneyLayout.findViewById(R.id.send_money_loading);
                ImageView closeDialog = sendMoneyLayout.findViewById(R.id.close_image_button);

                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AddBeneficiaryDialog);
                builder.setCancelable(false);
                builder.setView(sendMoneyLayout);

                final AlertDialog dialog = builder.create();
                dialog.show();
                closeDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(context,RemitterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        intent.putExtra(Constants.SESSION_ID,session_id);
                        intent.putExtra(Constants.REMITTER_MOBILE,mobileNumebr);
                        context.startActivity(intent);
                        ((RemitterActivity) context).finish();
                    }
                });

                amontEd.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        textInputLayout.setErrorEnabled(false);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        textInputLayout.setErrorEnabled(true);
                    }
                });
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String amount = amontEd.getText().toString().trim();
                        if (amount.isEmpty() || !isValidAmount(amount)){
                            textInputLayout.setError("Enter Amount between 10 to 5000");
                        }else {
                            loading.setVisibility(View.VISIBLE);
                            sendButton.setVisibility(View.INVISIBLE);
                            textInputLayout.setVisibility(View.INVISIBLE);
                            closeDialog.setEnabled(false);
                            beneficiaryViewModel.transferMoney(session_id,auth,mobileNumebr,remId,name,ifsc,account,benId,amount).observe((RemitterActivity) context, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    loading.setVisibility(View.INVISIBLE);
                                    sendButton.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                    Intent intent = new Intent(context,RemitterActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                    intent.putExtra(Constants.SESSION_ID,session_id);
                                    intent.putExtra(Constants.REMITTER_MOBILE,mobileNumebr);
                                    context.startActivity(intent);
                                    ((RemitterActivity) context).finish();
                                    Toast.makeText(context,s,Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button benConfirmButton = delBenConfirmLayout.findViewById(R.id.ben_del_confirm_button);
                Button benCancelButton = delBenConfirmLayout.findViewById(R.id.ben_del_cancel_button);
                ProgressBar confirmLoading = delBenConfirmLayout.findViewById(R.id.ben_del_confirm_loading);
                Group confirmGroup = delBenConfirmLayout.findViewById(R.id.del_confirm_group);

                final Button delete = deleteBenLayout.findViewById(R.id.delete_bene_button);
                final TextInputLayout textInputLayout = deleteBenLayout.findViewById(R.id.enter_delete_otp_layout);
                final TextInputEditText otpEdit = deleteBenLayout.findViewById(R.id.enter_delete_otp_input);
                final Group deleteGroup = deleteBenLayout.findViewById(R.id.delete_group);
                final ProgressBar loading = deleteBenLayout.findViewById(R.id.delete_ben_loading);

                //Creating Confirmation Dialog
                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(context);
                confirmBuilder.setCancelable(false);
                confirmBuilder.setView(delBenConfirmLayout);
                AlertDialog confirmDialog = confirmBuilder.create();
                confirmDialog.show();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setView(deleteBenLayout);
                final AlertDialog dialog = builder.create();

                benCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDialog.dismiss();
                        Intent intent = new Intent(context,RemitterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        intent.putExtra(Constants.SESSION_ID,session_id);
                        intent.putExtra(Constants.REMITTER_MOBILE,mobileNumebr);
                        context.startActivity(intent);
                        ((RemitterActivity) context).finish();
                    }
                });

                benConfirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        confirmLoading.setVisibility(View.VISIBLE);
                        confirmGroup.setVisibility(View.INVISIBLE);
                        beneficiaryViewModel.deleteBeneficiary(auth,benId,remId).observe((RemitterActivity)context, new Observer<AddBeneficiary>() {
                            @Override
                            public void onChanged(AddBeneficiary addBeneficiary) {
                                if (addBeneficiary != null){
                                    Log.e("Beneficiary Adapter: " ,"add beneficiary is full");
                                    String message = addBeneficiary.getMessage();
                                    Log.e("Beneficiary Adapter: " ,message);

                                    if (message.equals("Transaction Successful")){
                                        confirmDialog.dismiss();
                                        dialog.show();
                                    }
                                }else {
                                    Log.e("Beneficiary Adapter: " ,"Null");
                                }
                            }
                        });
                    }
                });

                otpEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        textInputLayout.setErrorEnabled(false);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        textInputLayout.setErrorEnabled(true);
                        if (s.length()<6){
                            textInputLayout.setError("Enter Valid OTP");
                        }else {
                            delete.setEnabled(true);
                        }
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String otp = otpEdit.getText().toString().trim();
                        if (otp.length()<1){
                            textInputLayout.setError("Enter OTP");
                        }else {
                            deleteGroup.setVisibility(View.GONE);
                            loading.setVisibility(View.VISIBLE);
                            beneficiaryViewModel.deleteBenValidation(auth,benId,remId,otp).observe((RemitterActivity) context, new Observer<AddBeneficiary>() {
                                @Override
                                public void onChanged(AddBeneficiary addBeneficiary) {
                                    loading.setVisibility(View.GONE);
                                    if (addBeneficiary != null){

                                        String data = addBeneficiary.getStatus();
                                        if (data.equals("SUCCESS")){
                                            dialog.dismiss();
                                            Toast.makeText(context,data,Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context,RemitterActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                            intent.putExtra(Constants.SESSION_ID,session_id);
                                            intent.putExtra(Constants.REMITTER_MOBILE,mobileNumebr);
                                            context.startActivity(intent);
                                            ((RemitterActivity) context).finish();
                                        }else {
                                          dialog.dismiss();
                                            Toast.makeText(context,addBeneficiary.getMessage(),Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context,RemitterActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                            intent.putExtra(Constants.SESSION_ID,session_id);
                                            intent.putExtra(Constants.REMITTER_MOBILE,mobileNumebr);
                                            context.startActivity(intent);
                                            ((RemitterActivity) context).finish();
                                        }
                                    }else {Log.e(
                                            "Beneficiary Adapter: " ,"Null");
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    //Set beneficiary list
    public void setBeneficiaryList(List<Beneficiary>beneficiaryList){
        this.beneficiaryList = beneficiaryList;
        notifyDataSetChanged();
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void setBenId(String benId) {
        this.benId = benId;
    }

    public void setRemId(String remId) {
        this.remId = remId;
        notifyDataSetChanged();
    }

    private boolean isValidAmount(String amt){
        int value = Integer.parseInt(amt);
        return value >= 10 && value <= 5000;
    }

    @Override
    public int getItemCount() {
        return beneficiaryList.size();
    }

    class BeneficiaryViewHolder extends RecyclerView.ViewHolder{

        TextView sucessName;
        TextView accountNum;
        TextView ifscCode;
        TextView successDate;

        ImageView transferButton;
        ImageView deleteButton;

        public BeneficiaryViewHolder(@NonNull View itemView) {
            super(itemView);

            sucessName = itemView.findViewById(R.id.ben_last_success_name);
            accountNum = itemView.findViewById(R.id.ben_account_number);
            ifscCode = itemView.findViewById(R.id.ben_ifsc);
            successDate = itemView.findViewById(R.id.ben_last_success_date);

            transferButton = itemView.findViewById(R.id.ben_transfer_button);
            deleteButton = itemView.findViewById(R.id.ben_delete_button);
        }
    }
}
