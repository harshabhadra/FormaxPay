package com.rechargeweb.rechargeweb.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AddBeneficiaryDialog);
                builder.setView(sendMoneyLayout);

                final AlertDialog dialog = builder.create();
                dialog.show();

                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String amount = amontEd.getText().toString().trim();
                        if (amount.isEmpty()){
                            textInputLayout.setError("Enter Amount");
                        }else {
                            loading.setVisibility(View.VISIBLE);
                            sendButton.setVisibility(View.GONE);
                            beneficiaryViewModel.transferMoney(session_id,auth,mobileNumebr,remId,name,ifsc,account,benId,amount).observe((RemitterActivity) context, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    loading.setVisibility(View.INVISIBLE);
                                    sendButton.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
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
                final TextView textView = deleteBenLayout.findViewById(R.id.delete_ben_message_tv);
                Button confirm = deleteBenLayout.findViewById(R.id.confirm_delete_ben);
                Button cancel = deleteBenLayout.findViewById(R.id.cancel_delete_ben);
                final Button delete = deleteBenLayout.findViewById(R.id.delete_bene_button);
                final TextInputLayout textInputLayout = deleteBenLayout.findViewById(R.id.enter_delete_otp_layout);
                final TextInputEditText otpEdit = deleteBenLayout.findViewById(R.id.enter_delete_otp_input);
                final Group confirmGroup = deleteBenLayout.findViewById(R.id.confirmation_group);
                final Group deleteGroup = deleteBenLayout.findViewById(R.id.delete_group);
                final ProgressBar loading = deleteBenLayout.findViewById(R.id.delete_ben_loading);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(deleteBenLayout);

                final AlertDialog dialog = builder.create();
                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmGroup.setVisibility(View.GONE);
                        loading.setVisibility(View.VISIBLE);

                        beneficiaryViewModel.deleteBeneficiary(auth,benId,remId).observe((RemitterActivity)context, new Observer<AddBeneficiary>() {
                            @Override
                            public void onChanged(AddBeneficiary addBeneficiary) {
                                loading.setVisibility(View.GONE);
                                if (addBeneficiary != null){
                                    Log.e("Beneficiary Adapter: " ,"add beneficiary is full");
                                    String message = addBeneficiary.getMessage();
                                    Log.e("Beneficiary Adapter: " ,message);

                                    if (message.equals("Transaction Successful")){
                                        textView.setText(message);
                                        confirmGroup.setVisibility(View.GONE);
                                        deleteGroup.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    Log.e("Beneficiary Adapter: " ,"Null");
                                }
                            }
                        });
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        delete.setVisibility(View.GONE);
                        loading.setVisibility(View.VISIBLE);
                        String otp = otpEdit.getText().toString().trim();
                        if (otp.isEmpty()){
                            textInputLayout.setError("Enter OTP");
                        }else {
                            beneficiaryViewModel.deleteBenValidation(auth,benId,remId,otp).observe((RemitterActivity) context, new Observer<AddBeneficiary>() {
                                @Override
                                public void onChanged(AddBeneficiary addBeneficiary) {
                                    if (addBeneficiary != null){
                                        dialog.dismiss();
                                        String data = addBeneficiary.getStatus();
                                        if (data.equals("SUCCESS")){
                                            Toast.makeText(context,data,Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(context,addBeneficiary.getMessage(),Toast.LENGTH_SHORT).show();
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
