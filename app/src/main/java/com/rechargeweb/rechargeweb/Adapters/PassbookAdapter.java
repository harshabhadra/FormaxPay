package com.rechargeweb.rechargeweb.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rechargeweb.rechargeweb.Model.Passbook;
import com.rechargeweb.rechargeweb.R;

import java.util.ArrayList;
import java.util.List;

public class PassbookAdapter extends RecyclerView.Adapter<PassbookAdapter.PassbookViewHolder> {

    private LayoutInflater inflater;
    private List<Passbook>passbookList = new ArrayList<>();
    private static final String TAG = PassbookAdapter.class.getSimpleName();
    OnPassbookItemClickListener clickListener;

    public PassbookAdapter(Context context, OnPassbookItemClickListener clickListener) {
        inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    public interface OnPassbookItemClickListener{
        void onPassBookItemClick(int position);
    }
    @NonNull
    @Override
    public PassbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PassbookViewHolder(inflater.inflate(R.layout.passbook_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PassbookViewHolder holder, int position) {

        if (passbookList!= null){

            holder.creaditTv.setText(passbookList.get(position).getCredit_amount());
            holder.debitTv.setText(passbookList.get(position).getDebit_amount());
            holder.debitTv.setTextColor(Color.RED);
            holder.walletTv.setText(passbookList.get(position).getNarration());
            holder.dateTv.setText(passbookList.get(position).getCreated_on());
        }
    }

    @Override
    public int getItemCount() {
        return passbookList.size();
    }

    public void setPassbookListt(List<Passbook>passbookList){
        this.passbookList = passbookList;
        notifyDataSetChanged();
    }

    public Passbook getPassBook(int position){
        return passbookList.get(position);
    }
    class PassbookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView creaditTv;
        TextView debitTv;
        TextView walletTv;
        TextView dateTv;

        public PassbookViewHolder(@NonNull View itemView) {
            super(itemView);

            creaditTv = itemView.findViewById(R.id.closing_balance_passbook);
            debitTv = itemView.findViewById(R.id.opening_balance_passbook);
            walletTv = itemView.findViewById(R.id.narration_passbook);
            dateTv = itemView.findViewById(R.id.traction_passbook);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            clickListener.onPassBookItemClick(position);
        }
    }
}
