package com.rechargeweb.rechargeweb.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
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
import androidx.core.content.ContextCompat;
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
    View view1;

    public BeneficiaryAdapter(Context context,String auth) {

        inflater = LayoutInflater.from(context);
        this.auth = auth;
        this.context = context;
        beneficiaryViewModel = ViewModelProviders.of((RemitterActivity)context).get(BeneficiaryViewModel.class);
    }

    @NonNull
    @Override
    public BeneficiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BeneficiaryViewHolder view =  new BeneficiaryViewHolder(inflater.inflate(R.layout.beneficiary_item,parent,false));

        view1 = inflater.inflate(R.layout.delete_beneficiary_layout, null);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull BeneficiaryViewHolder holder, final int position) {

        if (beneficiaryList != null){

            holder.sucessName.setText(beneficiaryList.get(position).getLastSuccessName());
            holder.accountNum.setText("A/C: " + beneficiaryList.get(position).getAccount());
            holder.ifscCode.setText("IFSC: " +beneficiaryList.get(position).getIfsc());
            holder.successDate.setText("Last Success: " + beneficiaryList.get(position).getLast_success_date());
        }

        holder.transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView textView = view1.findViewById(R.id.delete_ben_message_tv);
                Button confirm = view1.findViewById(R.id.confirm_delete_ben);
                Button cancel = view1.findViewById(R.id.cancel_delete_ben);
                final Button delete = view1.findViewById(R.id.delete_bene_button);
                final TextInputLayout textInputLayout = view1.findViewById(R.id.enter_delete_otp_layout);
                final TextInputEditText otpEdit = view1.findViewById(R.id.enter_delete_otp_input);
                final Group confirmGroup = view1.findViewById(R.id.confirmation_group);
                final Group deleteGroup = view1.findViewById(R.id.delete_group);
                final ProgressBar loading = view1.findViewById(R.id.delete_ben_loading);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view1);

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
                        benId = beneficiaryList.get(position).getId();
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

        Button transferButton;
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
