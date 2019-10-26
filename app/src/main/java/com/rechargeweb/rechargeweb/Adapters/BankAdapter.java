package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Model.Bank;
import com.rechargeweb.rechargeweb.R;

import java.util.ArrayList;
import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.BankViewHolder> {

    private LayoutInflater inflater;
    private List<Bank>bankList = new ArrayList<>();
    private OnBankItemClickListener clickListener;

    public BankAdapter(Context context, OnBankItemClickListener clickListener) {
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    public interface OnBankItemClickListener{
        void onBankItemClick(int position);
    }

    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BankViewHolder(inflater.inflate(R.layout.bank_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BankViewHolder holder, int position) {

        if (bankList!= null){
            holder.bankName.setText(bankList.get(position).getBankName());
            holder.ifscCode.setText(bankList.get(position).getIfscCode());
        }
    }

    //Get a single bank
    public Bank getBank(int position){
        return bankList.get(position);
    }

    //Set banklist
    public void setBankList(List<Bank>bankList){
        this.bankList = bankList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return bankList.size();
    }

    class BankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView bankName, ifscCode;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);

            bankName = itemView.findViewById(R.id.bank_name);
            ifscCode = itemView.findViewById(R.id.ifsc_code);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            clickListener.onBankItemClick(position);
        }
    }
}
